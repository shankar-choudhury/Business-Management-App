package com.spring2024project.Scheduler.constantValues;

import static com.spring2024project.Scheduler.validator.ValidatingMethods.*;

import java.util.*;

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
    NEW_HAMPSHIRE("NH", "NEWHAMPSHIRE"),
    NEW_JERSEY("NJ", "NEWJERSEY"),
    NEW_MEXICO("NM", "NEWMEXICO"),
    NEW_YORK("NY", "NEWYORK"),
    NORTH_CAROLINA("NC", "NORTHCAROLINA"),
    NORTH_DAKOTA("ND", "NORTHDAKOTA"),
    OHIO("OH", "OHIO"),
    OKLAHOMA("OK", "OKLAHOMA"),
    OREGON("OR", "OREGON"),
    PENNSYLVANIA("PA", "PENNSYLVANIA"),
    RHODE_ISLAND("RI", "RHODEISLAND"),
    SOUTH_CAROLINA("SC", "SOUTHCAROLINA"),
    SOUTH_DAKOTA("SD", "SOUTHDAKOTA"),
    TENNESSEE("TN", "TENNESSEE"),
    TEXAS("TX", "TEXAS"),
    UTAH("UT", "UTAH"),
    VERMONT("VT", "VERMONT"),
    VIRGINIA("VA", "VIRGINIA"),
    WASHINGTON("WA", "WASHINGTON"),
    WEST_VIRGINIA("WV", "WESTVIRGINIA"),
    WISCONSIN("WI", "WISCONSIN"),
    WYOMING("WY", "WYOMING"),
    EMPTY("", "");
    State(String abbreviation, String fullName) {
        Holder.ABBREVIATION_MAP.put(abbreviation, this);
        Holder.FULLNAME_MAP.put(fullName, this);
    }

    private static class Holder {
        static final Map<String,State> FULLNAME_MAP = new HashMap<>();
        static final Map<String,State> ABBREVIATION_MAP = new HashMap<>();
    }

    public static State find(String key) {
        return getState(
                normalize(
                        correctStateFormat(
                                verifyNonNullEmptyOrBlank(key))));
    }

    private static State getState(String normalizedKey) {
        assert Objects.nonNull(normalizedKey);
        assert !normalizedKey.isEmpty();
        assert !normalizedKey.isBlank();
        assert normalizedKey.matches("^[A-Za-z\\s]+$");

        return Objects.requireNonNullElse(
                Holder.ABBREVIATION_MAP.get(normalizedKey),
                Objects.requireNonNullElse(
                        Holder.FULLNAME_MAP.get(normalizedKey),
                        State.EMPTY));
    }

    private static String normalize(String key) {
        assert Objects.nonNull(key);
        assert !key.isEmpty();
        assert !key.isBlank();
        assert key.matches("^[A-Za-z\\s]+$");
        return key.trim().toUpperCase();
    }

}
