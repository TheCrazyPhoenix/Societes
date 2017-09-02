package com.github.thecrazyphoenix.societies.api.society;

import com.github.thecrazyphoenix.societies.api.society.SocietyElement;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.math.BigDecimal;

/**
 * Models an object of a society that is paid and taxed by the society.
 */
public interface Taxable extends SocietyElement {
    /**
     * Retrieves the fixed amount of money this Taxable pays in taxes.
     * This does not include territorial taxes.
     * @return The fixed tax rate paid daily.
     */
    BigDecimal getFixedTax();

    /**
     * Retrieves the amount of money this Taxable receives from the society.
     * @return The amount of money received daily.
     */
    BigDecimal getSalary();

    /**
     * Sets the fixed amount of money this Taxable pays in taxes.
     * @param newTax The new fixed tax rate paid daily.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setFixedTax(BigDecimal newTax, Cause cause);

    /**
     * Sets the amount of money this Taxable receives from the society.
     * @param newSalary The new salary received daily.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setSalary(BigDecimal newSalary, Cause cause);

    /**
     * Retrieves this Taxable's account.
     * @return The account the salary should be added to and the taxes should be taken from.
     */
    Account getAccount();
}
