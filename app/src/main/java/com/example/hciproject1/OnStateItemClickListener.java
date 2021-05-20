package com.example.hciproject1;

import com.example.hciproject1.components.StateItem;

public interface OnStateItemClickListener {

    void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState);

}
