package com.wesleymoy.mousetimer;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class TwentyFourHourDayConverter {
    public Period convert(Duration duration) {
        Period period = duration.toPeriod(PeriodType.dayTime());
        int hours = period.getHours();
        int days = hours / 24;
        hours %= 24;
        return period.withHours(hours).withDays(days);
    }
}
