package com.shorten.api.model;

import java.util.Date;

/**
 * Maps result of getAddedCountByDay
 */
public interface CountByDay {

    Date getDateAdded();

    int getCount();

}
