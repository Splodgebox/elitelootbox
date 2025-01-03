/*
 * Copyright (c) 2024. ElitePlugins License.
 * Permission is hereby granted to use this software (the "Software") for personal purposes or on private servers. Redistribution,
 * modification for distribution, or sale of this software is strictly prohibited.
 *
 * This software is provided "as is," without warranty of any kind, express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose, or non-infringement. In no event shall the author or copyright holder be liable for
 * any claim, damages, or other liability, whether in an action of contract, tort, or otherwise, arising from, out of, or in connection
 * with the Software or its use.
 *
 * See LICENSE.txt for details.
 */

package net.splodgebox.elitelootbox.models;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.splodgebox.elitelootbox.models.enums.StaticRewardType;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Builder
public class AnimationSchema {

    private final String id;
    private final List<String> schemaRows;
    private final Map<Character, String> schemaMap;
    private final int totalDuration;
    private final int shufflingInterval;
    private final StaticRewardType rewardType;

}
