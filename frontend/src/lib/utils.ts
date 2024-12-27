import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import { cubicOut } from "svelte/easing";
import type { TransitionConfig } from "svelte/transition";

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}

type FlyAndScaleParams = {
	y?: number;
	x?: number;
	start?: number;
	duration?: number;
};

export const flyAndScale = (
	node: Element,
	params: FlyAndScaleParams = { y: -8, x: 0, start: 0.95, duration: 150 }
): TransitionConfig => {
	const style = getComputedStyle(node);
	const transform = style.transform === "none" ? "" : style.transform;

	const scaleConversion = (
		valueA: number,
		scaleA: [number, number],
		scaleB: [number, number]
	) => {
		const [minA, maxA] = scaleA;
		const [minB, maxB] = scaleB;

		const percentage = (valueA - minA) / (maxA - minA);
		const valueB = percentage * (maxB - minB) + minB;

		return valueB;
	};

	const styleToString = (
		style: Record<string, number | string | undefined>
	): string => {
		return Object.keys(style).reduce((str, key) => {
			if (style[key] === undefined) return str;
			return str + `${key}:${style[key]};`;
		}, "");
	};

	return {
		duration: params.duration ?? 200,
		delay: 0,
		css: (t) => {
			const y = scaleConversion(t, [0, 1], [params.y ?? 5, 0]);
			const x = scaleConversion(t, [0, 1], [params.x ?? 0, 0]);
			const scale = scaleConversion(t, [0, 1], [params.start ?? 0.95, 1]);

			return styleToString({
				transform: `${transform} translate3d(${x}px, ${y}px, 0) scale(${scale})`,
				opacity: t
			});
		},
		easing: cubicOut
	};
};

export const convertToFormData = (payload: any, formData: FormData = new FormData(), parentKeys: string[] = []): FormData => {
	for (const [key, value] of Object.entries(payload)) {
		const currentKeys: string[] = [...parentKeys, key];
		const formDataKey: string = currentKeys.join('.');

		if (typeof value === 'object' && value !== null && !(value instanceof File)) {
			if (Array.isArray(value)) {
				// Check if the array values are IDs (numbers or strings)
				const containsOnlyIds = value.every(item => typeof item === 'number' || typeof item === 'string');

				if (containsOnlyIds) {
					// Append all IDs under the same key
					formData.append(formDataKey, value.join(','));
				} else {
					for (let i: number = 0; i < value.length; i++) {
						const arrayItemKey: string = `${formDataKey}[${i}]`;
						convertToFormData(value[i], formData, [...currentKeys, `${i}`]);
					}
				}
			} else {
				convertToFormData(value, formData, currentKeys);
			}
		} else {
			formData.append(formDataKey, value as string | Blob);
		}
	}

	return formData;
};