package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.rank.Taxable;

import java.math.BigDecimal;

/**
 * Base event for when a taxable's properties change.
 */
public interface TaxableChangeEvent extends SocietyChangeEvent {
    /**
     * Retrieves the taxable affected by this event.
     * This represents the old state of the taxable, except for the {@link Create} event.
     * @return The modified taxable.
     */
    Taxable getTaxable();

    /**
     * Called when a taxable is created.
     * @see MemberChangeEvent.Create
     */
    interface Create extends TaxableChangeEvent {}

    /**
     * Called when a taxable is destroyed.
     */
    interface Destroy extends TaxableChangeEvent {}

    /**
     * Called when a taxable's fixed tax changes.
     */
    interface ChangeFixedTax extends TaxableChangeEvent {
        /**
         * Retrieves the taxable's new fixed tax.
         * @return The new fixed tax rate per day.
         */
        BigDecimal getNewFixedTax();
    }

    /**
     * Called when a taxable's salary changes.
     */
    interface ChangeSalary extends TaxableChangeEvent {
        /**
         * Retrieves the taxable's new salary
         * @return The new salary of the taxable.
         */
        BigDecimal getNewSalary();
    }
}
