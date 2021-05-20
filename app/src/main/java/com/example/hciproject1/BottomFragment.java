package com.example.hciproject1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.util.Date;


public class BottomFragment extends Fragment {

    static int currentState;
    static boolean laterWashing;
    static TimePicker timePicker;
    static CountDownTimer waitTimer, cTimer;

    //Declare timer
    static CountDownTimer Timer1 = null;
    static CountDownTimer Timer2 = null;
    static CountDownTimer Timer3 = null;
    static CountDownTimer Timer4 = null;

    //cancel timer
    static void cancelTimerz() {
        if (Timer1 != null) {
            Timer1.cancel();
        }
        if (Timer2 != null) {
            Timer2.cancel();
        }
        if (Timer3 != null) {
            Timer3.cancel();
        }
        if (Timer4 != null) {
            Timer4.cancel();
        }
        if (cTimer != null) {
            cTimer.cancel();
        }
        if (waitTimer != null) {
            waitTimer.cancel();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_fragment, container, false);
        currentState = 1;

        timePicker = view.findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);

        final Fragment fragment = this;

        view.findViewById(R.id.button_abort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(1, fragment);
            }
        });

        view.findViewById(R.id.button_continue_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laterWashing = false;
                alterState(4, fragment);
            }
        });

        view.findViewById(R.id.button_continue_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laterWashing = true;
                alterState(25, fragment);
            }
        });

        view.findViewById(R.id.button_no_tablet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(1, fragment);
            }
        });

        view.findViewById(R.id.button_tablet_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(2, fragment);
//                if (!laterWashing) {
//                    alterState(4, fragment);
//                } else {
//                    alterState(25, fragment);
//                }
            }
        });

        view.findViewById(R.id.button_time_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(35, fragment);
            }
        });

        view.findViewById(R.id.button_abort_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(2, fragment);
            }
        });

        view.findViewById(R.id.abort_plan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(1, fragment);
            }
        });

        view.findViewById(R.id.wash_fucking_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(4, fragment);
            }
        });

        view.findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterInnerState(1, fragment);
            }
        });

        view.findViewById(R.id.no_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterInnerState(0, fragment);
            }
        });

        view.findViewById(R.id.yes_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTimerz();
                alterState(1, fragment);
            }
        });


        view.findViewById(R.id.last_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterState(1, fragment);
            }
        });

        return view;
    }

    /**
     * Clears the screen of any messages
     *
     * @param f the bottom fragment whose screen we are clearing
     */
    static void clearScreen(Fragment f) {
        f.getView().findViewById(R.id.screen_1_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_2_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_25_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_3_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_35_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_4_layout).setVisibility(View.GONE);
        f.getView().findViewById(R.id.screen_last_layout).setVisibility(View.GONE);
        ButtonsFragment.disableButtons();
    }

    static void washConfirmation(int washType, Fragment f) {
        String viewString = "";
        if (washType == 1) {
            viewString = "Ελαφρύ πλύσιμο";
        } else if (washType == 2) {
            viewString = "Κανονικό πλύσιμο";
        } else if (washType == 3) {
            viewString = "Ισχυρό πλύσιμο";
        }
        TextView tToAppear = f.getView().findViewById(R.id.fase2_text1);
        tToAppear.setText(viewString);
        f.getView().findViewById(R.id.screen_2_layout).setVisibility(View.VISIBLE);
    }

    static void alterState(int whichState, final Fragment f) {
        BottomFragment.clearScreen(f);
        cancelTimerz();
        if (whichState == 1) {//State 1 = perimenoume na epile3ei o xrhshts tupo plisimatos
            f.getView().findViewById(R.id.screen_1_layout).setVisibility(View.VISIBLE);
            TopFragment.visibleOnly(1);
            ButtonsFragment.enableButtons();
        } else if (whichState == 2) {//State 2 = Epivevaiwsi gia ton tropo plisimatos
            washConfirmation(FullscreenActivity.washCode, f);
            TopFragment.visibleOnly(1);
        } else if (whichState == 25) {
            f.getView().findViewById(R.id.screen_25_layout).setVisibility(View.VISIBLE);
            TopFragment.visibleOnly(1);
        } else if (whichState == 3) {//State 3 = Leme sto xristi na valei ti tableta ston pato tou
            f.getView().findViewById(R.id.screen_3_layout).setVisibility(View.VISIBLE);
            TopFragment.visibleOnly(1);
        } else if (whichState == 35) {
            f.getView().findViewById(R.id.screen_35_layout).setVisibility(View.VISIBLE);
            TopFragment.visibleOnly(1);
            int hours = timePicker.getHour();
            int minutes = timePicker.getMinute();
            int seconds = hours * 3600 + minutes * 60;
            Date dt = new Date();
            int hours_now = dt.getHours();
            int minutes_now = dt.getMinutes();
            int seconds_now = hours_now * 3600 + minutes_now * 60;
            long waiting_time;
            if (seconds <= seconds_now) {
                waiting_time = seconds + (24 * 3600 - seconds_now * 3600) - dt.getSeconds();
            } else {
                waiting_time = seconds - seconds_now - dt.getSeconds();
            }
            TextView tmp = f.getView().findViewById(R.id.programmed_hour_info);
            String str = minutes < 10 ? "0" : "";
            String tmpText = "Προγραμματισμένη ώρα πλυσίματος: " + hours + " : " + str + minutes;
            tmp.setText(tmpText);
            waitTimer = new CountDownTimer(1000 * waiting_time, 1000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    alterState(4, f);
                }
            };
            waitTimer.start();
        } else if (whichState == 4) {//State 4 = To plunthrio plenei kai o xrhsths mporei na epile3ei an tha to stamatisei i oxi
            f.getView().findViewById(R.id.screen_4_layout).setVisibility(View.VISIBLE);
            alterInnerState(0, f);
            TopFragment.visibleOnly(1);
            final int step = 10 * 60000 / 50;
            TopFragment.b1.mAnimDuration = step;
            TopFragment.b2.mAnimDuration = step;
            TopFragment.b3.mAnimDuration = step;
            TopFragment.b4.mAnimDuration = step;
            TopFragment.b5.mAnimDuration = step;

            Timer1 = new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    TopFragment.visibleOnly(2);
                    Timer2 = new CountDownTimer(step, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            TopFragment.visibleOnly(3);
                            Timer3 = new CountDownTimer(step, 1000) {
                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    TopFragment.visibleOnly(4);
                                    Timer4 = new CountDownTimer(step, 1000) {
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            TopFragment.visibleOnly(5);
                                            alterState(1234, f);
                                        }
                                    };
                                    Timer4.start();
                                }
                            };
                            Timer3.start();
                        }
                    };
                    Timer2.start();
                }
            };
            Timer1.start();
            final TextView timeRemaining = f.getView().findViewById(R.id.fase4_text1);
            cTimer = new CountDownTimer(3 * 10 * 60000 / 50 + 2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished < 30000) {
                        timeRemaining.setText("Απομένουν μερικά δευτερόλεπτα");
                    } else if (millisUntilFinished < 90000) {
                        timeRemaining.setText("Απομένει περίπου 1 λεπτό");
                    } else {
                        timeRemaining.setText("Απομένουν περίπου " + Math.round((double) millisUntilFinished / (double) 60000) + " λεπτά");
                    }
                }

                @Override
                public void onFinish() {
                }
            };
            cTimer.start();
        } else {
            TopFragment.visibleOnly(5);
            f.getView().findViewById(R.id.screen_last_layout).setVisibility(View.VISIBLE);
        }
        currentState = whichState;
    }

    static void alterInnerState(int code, Fragment f) {
        if (code == 0) {
            f.getView().findViewById(R.id.inner_layout).setVisibility(View.GONE);
            f.getView().findViewById(R.id.stop_button).setVisibility(View.VISIBLE);
            f.getView().findViewById(R.id.fase4_are_you_sure).setVisibility(View.GONE);
            f.getView().findViewById(R.id.fase4_text3).setVisibility(View.VISIBLE);
            f.getView().findViewById(R.id.fase4_text4).setVisibility(View.VISIBLE);
        } else {
            f.getView().findViewById(R.id.inner_layout).setVisibility(View.VISIBLE);
            f.getView().findViewById(R.id.stop_button).setVisibility(View.GONE);
            f.getView().findViewById(R.id.fase4_are_you_sure).setVisibility(View.VISIBLE);
            f.getView().findViewById(R.id.fase4_text3).setVisibility(View.GONE);
            f.getView().findViewById(R.id.fase4_text4).setVisibility(View.GONE);
        }
    }
}
