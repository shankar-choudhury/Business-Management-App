package com.spring2024project.Scheduler.constantValues;

import com.spring2024project.Scheduler.exception.StringValidationException;
import com.spring2024project.Scheduler.validatingMethods.AddressValidator;
import com.spring2024project.Scheduler.validatingMethods.StringValidator;

import java.util.*;

/**
 * Enum representing the states in the United States.
 * @Author Shankar Choudhury
 */
public enum State {
    ALABAMA("AL", "ALABAMA"),
    ALASKA("AK", "ALASKA"),
    ARIZONA("AZ", "ARIZONA"),
    ARKANSAS("AR", "ARKANSAS"),
    CALIFORNIA("CA", "CALIFORNIA"),
    COLORADO("CO", "COLORADO"),
    CONNECTICUT("CT", "CONNECTICUT"),
    DELAWARE("DE", "DELAWARE"),
    FLORIDA("FL", "FLORIDA"),
    GEORGIA("GA", "GEORGIA"),
    HAWAII("HI", "HAWAII"),
    IDAHO("ID", "IDAHO"),
    ILLINOIS("IL", "ILLINOIS"),
    INDIANA("IN", "INDIANA"),
    IOWA("IA", "IOWA"),
    KANSAS("KS", "KANSAS"),
    KENTUCKY("KY", "KENTUCKY"),
    LOUISIANA("LA", "LOUISIANA"),
    MAINE("ME", "MAINE"),
    MARYLAND("MD", "MARYLAND"),
    MASSACHUSETTS("MA", "MASSACHUSETTS"),
    MICHIGAN("MI", "MICHIGAN"),
    MINNESOTA("MN", "MINNESOTA"),
    MISSISSIPPI("MS", "MISSISSIPPI"),
    MISSOURI("MO", "MISSOURI"),
    MONTANA("MT", "MONTANA"),
    NEBRASKA("NE", "NEBRASKA"),
    NEVADA("NV", "NEVADA"),
    NEW_HAMPSHIRE("NH", "NEW HAMPSHIRE"),
    NEW_JERSEY("NJ", "NEW JERSEY"),
    NEW_MEXICO("NM", "NEW MEXICO"),
    NEW_YORK("NY", "NEW YORK"),
    NORTH_CAROLINA("NC", "NORTH CAROLINA"),
    NORTH_DAKOTA("ND", "NORTH DAKOTA"),
    OHIO("OH", "OHIO"),
    OKLAHOMA("OK", "OKLAHOMA"),
    OREGON("OR", "OREGON"),
    PENNSYLVANIA("PA", "PENNSYLVANIA"),
    RHODE_ISLAND("RI", "RHODE ISLAND"),
    SOUTH_CAROLINA("SC", "SOUTH CAROLINA"),
    SOUTH_DAKOTA("SD", "SOUTH DAKOTA"),
    TENNESSEE("TN", "TENNESSEE"),
    TEXAS("TX", "TEXAS"),
    UTAH("UT", "UTAH"),
    VERMONT("VT", "VERMONT"),
    VIRGINIA("VA", "VIRGINIA"),
    WASHINGTON("WA", "WASHINGTON"),
    WEST_VIRGINIA("WV", "WEST VIRGINIA"),
    WISCONSIN("WI", "WISCONSIN"),
    WYOMING("WY", "WYOMING"),
    EMPTY("", "");

    private final String abbreviation;
    private final String fullName;

    /**
     * Constructor for State enum.
     * @param abbreviation The abbreviation of the state.
     * @param fullName The full name of the state.
     */
    State(String abbreviation, String fullName) {
        this.abbreviation = abbreviation;
        this.fullName = fullName;
        Holder.ABBREVIATION_MAP.put(abbreviation, this);
        Holder.FULLNAME_MAP.put(fullName, this);
    }

    public String abbreviation() {
        return abbreviation;
    }

    public String fullName() {
        return fullName;
    }

    /**
     * Holder class for storing abbreviation and full name mappings.
     */
    private static class Holder {
        static final Map<String,State> FULLNAME_MAP = new HashMap<>();
        static final Map<String,State> ABBREVIATION_MAP = new HashMap<>();
    }

    /**
     * Get the State enum corresponding to the normalized key.
     * @param key The normalized abbreviation or full name of the state.
     * @return The corresponding State enum, or EMPTY if not found.
     */
    public static State getState(String key) {
        var normalized = normalize(key);

        return Objects.requireNonNullElse(
                Holder.ABBREVIATION_MAP.get(normalized),
                Objects.requireNonNullElse(
                        Holder.FULLNAME_MAP.get(normalized),
                        State.EMPTY));
    }

    /**
     * Normalize the input key by trimming and converting to uppercase.
     * @param key The input key to normalize.
     * @return The normalized key.
     */
    private static String normalize(String key) {
        assert Objects.nonNull(key);
        assert !key.isEmpty();
        assert !key.isBlank();
        assert key.matches("^[A-Za-z\\s]+$");
        return key.trim().toUpperCase();
    }

}