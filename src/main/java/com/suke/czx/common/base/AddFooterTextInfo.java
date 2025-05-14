package com.suke.czx.common.base;

import lombok.*;

/**
 * @author cuiyubao
 * @date 2025/1/10 16:29
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFooterTextInfo {

    /**
	 * 添加的文字
	 */
	private String text;

	/**
	 * 添加盖章关键字在宽度位置的比例
	 */
	private float positionXRatio;

	/**
	 * 添加盖章关键字在高度位置的比例
	 */
	private float positionYRatio;


}
