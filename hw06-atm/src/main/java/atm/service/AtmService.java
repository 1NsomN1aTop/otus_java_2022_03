package atm.service;

import atm.model.BanknoteType;

import java.util.Map;

public interface AtmService {

    void putMoney(Map<BanknoteType, Integer> banknotes);

    Map<BanknoteType, Integer> getMoney(long neededSum);

    long getBalance();
}
