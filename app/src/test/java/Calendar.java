import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.cjmkeke.mymemo.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class Calendar extends Activity {

    private MaterialCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendarView);

    }
}