package com.example.android.sunshine2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine2.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        if (viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        } else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Set icon as default for nwo
        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        iconView.setImageResource(R.mipmap.ic_launcher);

        // Set date
        String dateStr = cursor.getString(ForecastFragment.COL_WEATHER_DATE);
        TextView dateTextView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateTextView.setText(Utility.getFriendlyDayString(context, Long.parseLong(dateStr)));

        // Set forecast
        String forecastStr = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        TextView forecastTextView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        forecastTextView.setText(forecastStr);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Set high temp
        Double highTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        TextView highTempTextView = (TextView) view.findViewById(R.id.list_item_high_textview);
        highTempTextView.setText(Utility.formatTemperature(highTemp, isMetric));

        // Set low temp
        Double lowTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        TextView lowTempTextView = (TextView) view.findViewById(R.id.list_item_low_textview);
        lowTempTextView.setText(Utility.formatTemperature(lowTemp, isMetric));
    }
}
