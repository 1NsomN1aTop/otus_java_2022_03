package atm.service;

import atm.model.Atm;
import atm.model.BanknoteType;

import java.util.Map;

public class AtmServiceImpl implements AtmService {
    private final Atm atm;

    public AtmServiceImpl(Atm atm) {
        this.atm = atm;
    }

    @Override
    public void putMoney(Map<BanknoteType, Integer> banknotes) {
        atm.putMoney(banknotes);
    }

    @Override
    public Map<BanknoteType, Integer> getMoney(long neededSum) {
        return atm.getMoney(neededSum);
    }

    @Override
    public long getBalance() {
        return atm.getBalance();
    }
}