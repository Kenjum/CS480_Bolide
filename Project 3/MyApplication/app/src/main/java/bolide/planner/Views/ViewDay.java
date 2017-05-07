package bolide.planner.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import bolide.planner.R;

public class ViewDay extends LinearLayout {

    private static final int DAYS_COUNT = 42;
    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    // internal components
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;

    public ViewDay(Context context) {
        super(context);
    }
    ViewDay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public ViewDay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_day_layout, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();
        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                previousCalendarDate();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
                nextCalendarDate();
            }
        });
    }
    private void previousCalendarDate(){
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        txtDate.setText(displayDateInString(currentDate.getTime()));
        updateCalendar();
    }
    private void nextCalendarDate(){
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        txtDate.setText(displayDateInString(currentDate.getTime()));
        updateCalendar();
    }
    private String displayDateInString(Date mDate){
        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy", Locale.ENGLISH);
        return formatter.format(mDate);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events) {

    }
}