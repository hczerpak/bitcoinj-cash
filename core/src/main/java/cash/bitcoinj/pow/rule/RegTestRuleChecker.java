package cash.bitcoinj.pow.rule;

import cash.bitcoinj.core.*;
import cash.bitcoinj.pow.AbstractPowRulesChecker;
import cash.bitcoinj.store.BlockStore;
import cash.bitcoinj.store.BlockStoreException;

public class RegTestRuleChecker extends AbstractPowRulesChecker {
    public RegTestRuleChecker(NetworkParameters networkParameters) {
        super(networkParameters);
    }

    public void checkRules(StoredBlock storedPrev, Block nextBlock, BlockStore blockStore,
                                    AbstractBlockChain blockChain) throws VerificationException, BlockStoreException {
        // always pass
    }
}
