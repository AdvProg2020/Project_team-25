package Store.Controller;

import Store.Model.Offer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OffersController {

    public static ArrayList<Offer> getFilteredList(ArrayList<String> filters) {
        ArrayList<Offer> offers = Offer.getAllOffers();
        try {
            return offers.stream()
                    .filter(product -> product.getFilters().containsAll(filters))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return offers;
        }

    }

    public static ArrayList<Offer> sortOffers(String mode, ArrayList<String> filters) {
        if (mode.equalsIgnoreCase("time of starting")) {
            return timeOfStartSort(filters);
        } else {
            return timeOfEndingSort(filters);
        }

    }

    private static ArrayList<Offer> timeOfStartSort(ArrayList<String> filters) {
        ArrayList<Offer> offers = new ArrayList<Offer>();
        offers.addAll(getFilteredList(filters));
        try {
            return offers.stream()
                    .sorted(Comparator.comparing(Offer::getStartingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return offers;
        }
    }

    private static ArrayList<Offer> timeOfEndingSort(ArrayList<String> filters) {
        ArrayList<Offer> offers = new ArrayList<Offer>();
        offers.addAll(getFilteredList(filters));
        try {
            return offers.stream()
                    .sorted(Comparator.comparing(Offer::getEndingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return offers;
        }
    }

    public static ArrayList<Offer> sort(String mode, ArrayList<Offer> offers) {
        if (mode.equalsIgnoreCase("time of starting")) {
            return sortByStartingTime(offers);
        } else if (mode.equalsIgnoreCase("time of ending")) {
            return sortByEndingTime(offers);
        }
        return offers;
    }

    private static ArrayList<Offer> sortByStartingTime(ArrayList<Offer> offers) {
        ArrayList<Offer> result = new ArrayList<Offer>();
        result.addAll(offers);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(Offer::getStartingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<Offer> sortByEndingTime(ArrayList<Offer> offers) {
        ArrayList<Offer> result = new ArrayList<Offer>();
        result.addAll(offers);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(Offer::getEndingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }
}
