package com.wim.wim;

/**
 * Colors of previous views indicates if user chose right or wrong answer.
 * States indicates if the color of element should be:
 * R - right (ie. green color)
 * W - wrong (ie. red color)
 * N - neutral (ie. black)
 */
public enum PreviousColorCombination {
    NNRR,
    NNWW,
    RRNN,
    WWNN,
    NNNN;
}
