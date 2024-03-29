package atm.model;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;

public class AtmImpl implements Atm {

    private final NavigableMap<BanknoteType, AtmCell> banknotes = new TreeMap<>(Comparator.comparing(BanknoteType::getValue));

    @Override
    public void putMoney(Map<BanknoteType, Integer> banknotes) {
        banknotes.forEach(this::putBanknote);
    }

    @Override
    public Map<BanknoteType, Integer> getMoney(long neededSum) {
        if (neededSum <= 0) {
            throw new InvalidParameterException();
        }

        if (neededSum > getBalance()) {
            throw new InvalidParameterException();
        }

        final int minBanknoteValue = banknotes.firstEntry().getValue().getBanknoteValue();
        if (!isBanknotesSuitable(neededSum, minBanknoteValue)) {
            throw new InvalidParameterException("Not enough suitable banknotes");
        }

        Map<BanknoteType, Integer> popBanknotes = findSuitableBanknotes(neededSum);

        removeBanknotesFromCells(popBanknotes);

        return popBanknotes;
    }

    private void removeBanknotesFromCells(Map<BanknoteType, Integer> popBanknotes) {
        for (var popBanknote : popBanknotes.entrySet()) {
            banknotes.get(popBanknote.getKey()).popBanknote(popBanknote.getValue());
        }
    }

    private Map<BanknoteType, Integer> findSuitableBanknotes(long neededSum) {
        long restSum = neededSum;
        Map<BanknoteType, Integer> popBanknotes = new HashMap<>();
        for (var banknote : banknotes.descendingMap().entrySet()) {
            var banknoteValue = banknote.getValue().getBanknoteValue();
            var banknoteCount = banknote.getValue().getBanknoteCount();

            final int neededCountBanknotes = (int) (restSum / banknoteValue);
            if (neededCountBanknotes > 0 && neededCountBanknotes <= banknoteCount) {
                popBanknotes.put(banknote.getValue().getBanknoteType(), neededCountBanknotes);
                restSum -= (long) neededCountBanknotes * banknoteValue;
            }
        }
        if (restSum != 0) {
            throw new InvalidParameterException();
        }
        return popBanknotes;
    }

    @Override
    public long getBalance() {
        long allSum = 0L;
        for (var cell : banknotes.entrySet()) {
            allSum += cell.getValue().getTotalBalance();
        }
        return allSum;
    }

    private void putBanknote(BanknoteType bankNoteType, int count) {
        var atmCell = banknotes.get(bankNoteType);
        if (atmCell == null) {
            atmCell = new AtmCell(bankNoteType);
            banknotes.put(bankNoteType, atmCell);
        }
        atmCell.putBanknote(count);
    }

    private boolean isBanknotesSuitable(long neededSum, int banknoteValue) {
        return BigDecimal.valueOf((double) neededSum / (double) banknoteValue)
                .remainder(BigDecimal.ONE).equals(BigDecimal.valueOf(0.0));
    }
}