package module3;

//Java utilities libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

//import java.util.Collections;
//import java.util.Comparator;
//Processing library
//Unfolding libraries
//Parsing library

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

    // You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
    public static final int KEY_START_X = 25;
    public static final int MINOR_KEY_Y = 160;
    public static final int MODERATE_KEY_Y = 120;
    public static final int MAJOR_KEY_Y = 80;

    /** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";


	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 25, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 25, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}

	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);

	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

        for (PointFeature earthquake : earthquakes) {
            markers.add(createMarker(earthquake));
        }

        map.addMarkers(markers);
    }

	// A suggested helper method that takes in an earthquake feature and
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
        SimplePointMarker marker = new SimplePointMarker();
        marker.setLocation(feature.getLocation());

        float magnitude = Float.parseFloat(feature.getProperty("magnitude").toString());
        marker.setColor(getColorForMagnitude(magnitude));
        marker.setRadius(getRadiusForMagnitude(magnitude));
        return marker;
    }

    private float getRadiusForMagnitude(float magnitude) {
        if (magnitude < THRESHOLD_LIGHT) {
            return 8f;
        } else if (magnitude < THRESHOLD_MODERATE) {
            return 14f;
        } else {
            return 20f;
        }
    }

    private int getColorForMagnitude(float magnitude) {
        if (magnitude < THRESHOLD_LIGHT) {
            return color(0, 0, 255);
        } else if (magnitude < THRESHOLD_MODERATE) {
            return color(255, 255, 0);
        } else {
            return color(255, 0, 0);
        }
    }

    public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey()
	{
		// Remember you can use Processing's graphics methods here;
        fill(220, 220, 220);
        rect(10, 25, 180, 350, 10);
        fill(getColorForMagnitude(THRESHOLD_LIGHT - 1));
        float r = getRadiusForMagnitude(THRESHOLD_LIGHT - 1);
        ellipse(KEY_START_X, MINOR_KEY_Y, r, r);
        fill(getColorForMagnitude(THRESHOLD_MODERATE - 1));
        r = getRadiusForMagnitude(THRESHOLD_MODERATE - 1);
        ellipse(KEY_START_X, MODERATE_KEY_Y, r, r);
        fill(getColorForMagnitude(THRESHOLD_MODERATE + 1));
        r = getRadiusForMagnitude(THRESHOLD_MODERATE + 1);
        ellipse(KEY_START_X, MAJOR_KEY_Y, r, r);

        fill(0f);
        text("Below 4.0", KEY_START_X + 20, MINOR_KEY_Y);
        text("4.0+ Magnitude", KEY_START_X + 20, MODERATE_KEY_Y);
        text("5.0+ Magnitude", KEY_START_X + 20, MAJOR_KEY_Y);

        text("Earthquake Key", 50, 40);

	}
}
