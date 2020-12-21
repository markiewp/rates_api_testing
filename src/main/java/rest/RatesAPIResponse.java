package rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RatesAPIResponse {
    private String base;
    private String date;
    private String error;

    Map<String, Object> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Object> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "RatesAPIResponse[" +
                "base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                ']';
    }

    public List<String> getCurrencies() {
        List<String> responseCurrencies = new ArrayList<>(rates.keySet());
        responseCurrencies.add(getBase());
        return responseCurrencies;
    }
}
