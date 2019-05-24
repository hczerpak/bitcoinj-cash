/*
 * Copyright 2018 the bitcoinj-cash developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cash.bitcoinj.pow.factory;

import cash.bitcoinj.core.Block;
import cash.bitcoinj.core.NetworkParameters;
import cash.bitcoinj.core.StoredBlock;
import cash.bitcoinj.params.AbstractBitcoinNetParams;
import cash.bitcoinj.params.TestNet3Params;
import cash.bitcoinj.pow.AbstractPowRulesChecker;
import cash.bitcoinj.pow.AbstractRuleCheckerFactory;
import cash.bitcoinj.pow.RulesPoolChecker;
import cash.bitcoinj.pow.rule.DifficultyTransitionPointRuleChecker;
import cash.bitcoinj.pow.rule.EmergencyDifficultyAdjustmentRuleChecker;
import cash.bitcoinj.pow.rule.LastNonMinimalDifficultyRuleChecker;
import cash.bitcoinj.pow.rule.MinimalDifficultyNoChangedRuleChecker;

public class EDARuleCheckerFactory extends AbstractRuleCheckerFactory {

    public EDARuleCheckerFactory(NetworkParameters parameters) {
        super(parameters);
    }

    @Override
    public RulesPoolChecker getRuleChecker(StoredBlock storedPrev, Block nextBlock) {
        if (AbstractBitcoinNetParams.isDifficultyTransitionPoint(storedPrev, networkParameters)) {
            return getTransitionPointRulesChecker();
        } else {
            return getNoTransitionPointRulesChecker(storedPrev, nextBlock);
        }
    }

    private RulesPoolChecker getTransitionPointRulesChecker() {
        RulesPoolChecker rulesChecker = new RulesPoolChecker(networkParameters);
        rulesChecker.addRule(new DifficultyTransitionPointRuleChecker(networkParameters));
        return rulesChecker;
    }

    private RulesPoolChecker getNoTransitionPointRulesChecker(StoredBlock storedPrev, Block nextBlock) {
        RulesPoolChecker rulesChecker = new RulesPoolChecker(networkParameters);
        if (isTestNet() && TestNet3Params.isValidTestnetDateBlock(nextBlock)) {
            rulesChecker.addRule(new LastNonMinimalDifficultyRuleChecker(networkParameters));
        } else {
            if (AbstractPowRulesChecker.hasEqualDifficulty(
                    storedPrev.getHeader().getDifficultyTarget(), networkParameters.getMaxTarget())) {
                rulesChecker.addRule(new MinimalDifficultyNoChangedRuleChecker(networkParameters));
            } else {
                rulesChecker.addRule(new EmergencyDifficultyAdjustmentRuleChecker(networkParameters));
            }
        }
        return rulesChecker;
    }

}
