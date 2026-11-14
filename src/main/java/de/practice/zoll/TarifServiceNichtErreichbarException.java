package de.practice.zoll;

public class TarifServiceNichtErreichbarException extends RuntimeException {
    public TarifServiceNichtErreichbarException(String message) {
        super(message);
    }
}
