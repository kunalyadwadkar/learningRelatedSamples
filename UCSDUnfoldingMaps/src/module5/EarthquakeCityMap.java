package module5;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.*;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 *
 * @author Your name here
 *         Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

    // We will use member variables, instead of local variables, to store the data
    // that the setup and draw methods will need to access (as well as other methods)
    // You will use many of these variables, but the only one you should need to add
    // code to modify is countryQuakes, where you will store the number of earthquakes
    // per country.

    // You can ignore this.  It's to get rid of eclipse warnings
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFILINE, change the value of this variable to true
    private static final boolean offline = false;

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";

    static List<String> airportsToShowList = Arrays.asList(
            "\"GKA\"",
            "\"ORD\"",
            "\"IAD\"",
            "\"BOM\"",
            "\"PNQ\"",
            "\"DEL\"",
            "\"CDG\"",
            "\"LHR\"",
            "\"GLA\"",
            "\"JFK\"",
            "\"LGA\"",
            "\"EDI\"",
            "\"SEA\"",
            "\"SFO\"",
            "\"SJC\"",
            "\"ZRH\"",
            "\"FCO\"",
            "\"MXP\"",
            "\"FRA\"",
            "\"SIN\"",
            "\"KUL\""
    );

    static Set<String> airportVisitedSet = new HashSet<>();

    static {
        for (String s : airportsToShowList) {
            airportVisitedSet.add(s);
        }
    }

    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

    // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";
    private String airportFile = "airports.dat";

    // The map
    private UnfoldingMap map;

    // Markers for each city
    private List<Marker> cityMarkers;
    // Markers for each earthquake
    private List<Marker> quakeMarkers;
    // Markers for each airport;
    private List<Marker> airportMarkers;

    // A List of country markers
    private List<Marker> countryMarkers;

    private ToggleButton earthquakesToggle;
    private ToggleButton airportsToggle;

    // NEW IN MODULE 5
    private CommonMarker lastSelected;
    private CommonMarker lastClicked;

    public void setup() {
        // (1) Initializing canvas and map tiles
        size(900, 700, OPENGL);
        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "quiz2.atom";  // The same feed, but saved August 7, 2015
        } else {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }
        MapUtils.createDefaultEventDispatcher(this, map);


        // (2) Reading in earthquake data and geometric properties
        //     STEP 1: load country features and markers
        List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryMarkers = MapUtils.createSimpleMarkers(countries);

        //     STEP 2: read in city data
        List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
        cityMarkers = new ArrayList<Marker>();
        for (Feature city : cities) {
            cityMarkers.add(new CityMarker(city));
        }

        //     STEP 3: read in earthquake RSS feed
        List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
        quakeMarkers = new ArrayList<Marker>();

        for (PointFeature feature : earthquakes) {
            //check if LandQuake
            if (isLand(feature)) {
                quakeMarkers.add(new LandQuakeMarker(feature));
            }
            // OceanQuakes
            else {
                quakeMarkers.add(new OceanQuakeMarker(feature));
            }
        }

        // Now read in airport data feed.
        List<PointFeature> airports = ParseFeed.parseAirports(this, airportFile);
        airportMarkers = new ArrayList<>();

        for (PointFeature airportPf : airports) {
            AirportMarker airportMarker = new AirportMarker(airportPf);
            if (airportVisitedSet.contains(airportMarker.getProperty("code").toString())) {
                airportMarkers.add(airportMarker);
            }
        }


        earthquakesToggle = ToggleButton.withLocation(this, 40, 325).addTitleFetcher(sel -> {
            if (sel) {
                return "Show earthquakes";
            } else {
                return "Hide earthquakes";
            }
        }).addStyleSetter(new ToggleButton.StyleSetter() {
            @Override
            public void before(PApplet p) {
                p.pushStyle();
                p.fill(144, 144, 144);
            }

            @Override
            public void after(PApplet p) {
                p.popStyle();
            }
        });

        airportsToggle = ToggleButton.withLocation(this, 40, 350).addTitleFetcher(sel -> {
            if (sel) {
                return "Show airports";
            } else {
                return "Hide airports";
            }
        }).addStyleSetter(new ToggleButton.StyleSetter() {
            @Override
            public void before(PApplet p) {
                p.pushStyle();
                p.fill(144, 144, 144);
            }

            @Override
            public void after(PApplet p) {
                p.pushStyle();
            }
        });


        sortAndPrint(15);
        // could be used for debugging
//	    printQuakes();


        // (3) Add markers to map
        //     NOTE: Country markers are not added to the map.  They are used
        //           for their geometric properties
        map.addMarkers(quakeMarkers);
        map.addMarkers(cityMarkers);
        map.addMarkers(airportMarkers);

    }  // End setup

    private void sortAndPrint(int numToPrint) {
        Object[] earthquakeMarkers = quakeMarkers.toArray();

        Arrays.sort(earthquakeMarkers);

        for (int i = 0; i < earthquakeMarkers.length && i < numToPrint; i++) {
            EarthquakeMarker earthquakeMarker = (EarthquakeMarker) earthquakeMarkers[i];
            String msg = earthquakeMarker.getTitle();
            System.out.println(msg);
        }
    }


    public void draw() {
        background(0);
        map.draw();
        addKey();
        earthquakesToggle.draw();
        airportsToggle.draw();
    }

    /**
     * Event handler that gets called automatically when the
     * mouse moves.
     */
    @Override
    public void mouseMoved() {
        // clear the last selection
        if (lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected = null;

        }
        selectMarkerIfHover(quakeMarkers);
        selectMarkerIfHover(cityMarkers);
        selectButtonIfHover(earthquakesToggle, airportsToggle);
    }

    private void selectButtonIfHover(ToggleButton... toggles) {
        if (toggles != null && toggles.length != 0) {
            for (ToggleButton button : toggles) {
                if (button.isInside(mouseX, mouseY)) {
                    button.setSelected(true);
                } else {
                    button.setSelected(false);
                }
            }
        }
    }

    // If there is a marker under the cursor, and lastSelected is null
    // set the lastSelected to be the first marker found under the cursor
    // Make sure you do not select two markers.
    //
    private void selectMarkerIfHover(List<Marker> markers) {
        for (Marker marker : markers) {
            if (marker.isInside(map, mouseX, mouseY) && lastSelected == null) {
                lastSelected = (CommonMarker) marker;
                marker.setSelected(true);
            }
        }
    }

    /**
     * The event handler for mouse clicks
     * It will display an earthquake and its threat circle of cities
     * Or if a city is clicked, it will display all the earthquakes
     * where the city is in the threat circle
     */
    @Override
    public void mouseClicked() {

        if (earthquakesToggle.isSelected()) {
            if (earthquakesToggle.isSetToHide()) {
                earthquakesToggle.setIsSetToHide(false);
                unhideMarkers();
            } else {
                earthquakesToggle.setIsSetToHide(true);
                hideAll();
            }
        } else if (airportsToggle.isSelected()) {
            if (airportsToggle.isSetToHide()) {
                airportsToggle.setIsSetToHide(false);
                showAirports();
            } else {
                airportsToggle.setIsSetToHide(true);
                hideAirports();
            }
        }

        if (lastClicked != null) {

            for (Marker quakeMarker : quakeMarkers) {
                if (quakeMarker.isSelected()) {
                    quakeMarker.setSelected(false);
                }
            }

            for (Marker cityMarker : cityMarkers) {
                if (cityMarker.isSelected()) {
                    cityMarker.setSelected(false);
                }
            }

            lastClicked = null;
            unhideMarkers();
        } else {
            for (Marker cityMarker : cityMarkers) {
                if (cityMarker.isSelected()) {
                    lastClicked = (CommonMarker) cityMarker;
                    hideAll();
                    cityMarker.setHidden(false);
                    unhideQuakesForCity(cityMarker);
                    break;
                }
            }

            for (Marker quakeMarker : quakeMarkers) {
                if (quakeMarker.isSelected()) {
                    lastClicked = (CommonMarker) quakeMarker;
                    hideAll();
                    quakeMarker.setHidden(false);
                    unhideCitiesForQuake((EarthquakeMarker) quakeMarker);
                    break;
                }
            }
        }
    }


    private void unhideCitiesForQuake(EarthquakeMarker qm) {

        for (Marker cm : cityMarkers) {
            if (qm.threatCircle() >= cm.getDistanceTo(qm.getLocation())) {
                cm.setHidden(false);
                break;
            }
        }
    }

    private void unhideQuakesForCity(Marker cityMarker) {
        for (Marker marker : quakeMarkers) {
            EarthquakeMarker qm = (EarthquakeMarker) marker;

            if (cityMarker.getDistanceTo(qm.getLocation()) <= qm.threatCircle()) {
                qm.setHidden(false);
            }
        }
    }


    // loop over and unhide all markers
    private void unhideMarkers() {
        for (Marker marker : quakeMarkers) {
            marker.setHidden(false);
        }

        for (Marker marker : cityMarkers) {
            marker.setHidden(false);
        }

    }

    private void showAirports() {
        for (Marker airportMarker : airportMarkers) {
            airportMarker.setHidden(false);
        }
    }

    private void hideAll() {


        for (Marker cityMarker : cityMarkers) {
            cityMarker.setHidden(true);
        }

        for (Marker quakeMarker : quakeMarkers) {
            quakeMarker.setHidden(true);
        }

    }

    private void hideAirports() {
        for (Marker airportMarker : airportMarkers) {
            airportMarker.setHidden(true);
        }

    }

    // helper method to draw key in GUI
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240);

        int xbase = 25;
        int ybase = 50;

        rect(xbase, ybase, 150, 350);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(12);
        text("Earthquake Key", xbase + 25, ybase + 25);

        fill(150, 30, 30);
        int tri_xbase = xbase + 35;
        int tri_ybase = ybase + 50;
        triangle(tri_xbase, tri_ybase - CityMarker.TRI_SIZE, tri_xbase - CityMarker.TRI_SIZE,
                tri_ybase + CityMarker.TRI_SIZE, tri_xbase + CityMarker.TRI_SIZE,
                tri_ybase + CityMarker.TRI_SIZE);

        fill(0, 0, 0);
        textAlign(LEFT, CENTER);
        text("City Marker", tri_xbase + 15, tri_ybase);

        text("Land Quake", xbase + 50, ybase + 70);
        text("Ocean Quake", xbase + 50, ybase + 90);
        text("Size ~ Magnitude", xbase + 25, ybase + 110);

        fill(255, 255, 255);
        ellipse(xbase + 35,
                ybase + 70,
                10,
                10);
        rect(xbase + 35 - 5, ybase + 90 - 5, 10, 10);

        fill(color(255, 255, 0));
        ellipse(xbase + 35, ybase + 140, 12, 12);
        fill(color(0, 0, 255));
        ellipse(xbase + 35, ybase + 160, 12, 12);
        fill(color(255, 0, 0));
        ellipse(xbase + 35, ybase + 180, 12, 12);

        textAlign(LEFT, CENTER);
        fill(0, 0, 0);
        text("Shallow", xbase + 50, ybase + 140);
        text("Intermediate", xbase + 50, ybase + 160);
        text("Deep", xbase + 50, ybase + 180);

        text("Past hour", xbase + 50, ybase + 200);

        fill(255, 255, 255);
        int centerx = xbase + 35;
        int centery = ybase + 200;
        ellipse(centerx, centery, 12, 12);

        strokeWeight(2);
        line(centerx - 8, centery - 8, centerx + 8, centery + 8);
        line(centerx - 8, centery + 8, centerx + 8, centery - 8);

    }


    // Checks whether this quake occurred on land.  If it did, it sets the
    // "country" property of its PointFeature to the country where it occurred
    // and returns true.  Notice that the helper method isInCountry will
    // set this "country" property already.  Otherwise it returns false.
    private boolean isLand(PointFeature earthquake) {

        // IMPLEMENT THIS: loop over all countries to check if location is in any of them
        // If it is, add 1 to the entry in countryQuakes corresponding to this country.
        for (Marker country : countryMarkers) {
            if (isInCountry(earthquake, country)) {
                return true;
            }
        }

        // not inside any country
        return false;
    }

    // prints countries with number of earthquakes
    private void printQuakes() {
        int totalWaterQuakes = quakeMarkers.size();
        for (Marker country : countryMarkers) {
            String countryName = country.getStringProperty("name");
            int numQuakes = 0;
            for (Marker marker : quakeMarkers) {
                EarthquakeMarker eqMarker = (EarthquakeMarker) marker;
                if (eqMarker.isOnLand()) {
                    if (countryName.equals(eqMarker.getStringProperty("country"))) {
                        numQuakes++;
                    }
                }
            }
            if (numQuakes > 0) {
                totalWaterQuakes -= numQuakes;
                System.out.println(countryName + ": " + numQuakes);
            }
        }
        System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
    }


    // helper method to test whether a given earthquake is in a given country
    // This will also add the country property to the properties of the earthquake feature if
    // it's in one of the countries.
    // You should not have to modify this code
    private boolean isInCountry(PointFeature earthquake, Marker country) {
        // getting location of feature
        Location checkLoc = earthquake.getLocation();

        // some countries represented it as MultiMarker
        // looping over SimplePolygonMarkers which make them up to use isInsideByLoc
        if (country.getClass() == MultiMarker.class) {

            // looping over markers making up MultiMarker
            for (Marker marker : ((MultiMarker) country).getMarkers()) {

                // checking if inside
                if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));

                    // return if is inside one
                    return true;
                }
            }
        }

        // check if inside country represented by SimplePolygonMarker
        else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));

            return true;
        }
        return false;
    }

}
