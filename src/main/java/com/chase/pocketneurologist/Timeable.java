package com.chase.pocketneurologist;

import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by chase on 2/7/17.
 *
 * Purpose: Interface that defines the methods that must be implemented if a test is going have a set time.
 *
 */

public interface Timeable {

    public long getTestTime();
    public void endTiming();
    public void addProgress(float addedProgress);
}
