package nl.ou.applablib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bvgastel on 22-05-14.
 */
public class WizardFragment extends DialogFragment {
    private static final String TAG = "WizardFragment";
    private static final boolean DEBUG = false && BuildConfig.DEBUG;

    private static final String nsApp = "http://schemas.android.com/apk/res-auto";
    private static final String nsAndroid = "http://schemas.android.com/apk/res/android";

    private static final String VERSION_KEY = "PREFS_WIZARD_VERSION_CODE";

    private long animationDuration = 1;

    static public WizardFragment newInstanceIfNeeded(Context cxt, int resOfFragment) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(cxt);

        int lastVersion = sp.getInt(VERSION_KEY, 0);
        //Log.d(TAG, "lastVersion: " + lastVersion);
        int thisVersion = 0;
        try {
            thisVersion = cxt.getPackageManager().getPackageInfo(cxt.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            thisVersion = 0;
            Log.e(TAG, "could not get version name from manifest!");
            e.printStackTrace();
        }
        // FIXME: if shown on upgrade, filter the screens accordingly.
        //if (lastVersion == thisVersion)
        //if (lastVersion != 0)
        //    return null;

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(VERSION_KEY, thisVersion);
        editor.apply();

        return newInstance(resOfFragment);
    }

    static public WizardFragment newInstance(int resOfFragment) {
        WizardFragment f = new WizardFragment();
        Bundle args = new Bundle();
        args.putInt("resource", resOfFragment);
        f.setArguments(args);
        return f;
    }

    public static class Screen {
        int id;
        int icon;
        String title;
        String description;
    }

    Drawable background;
    boolean light = false;
    int width = Integer.MAX_VALUE;
    int height = Integer.MAX_VALUE;
    ArrayList<Screen> screens = new ArrayList<Screen>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        animationDuration = Math.max(getResources().getInteger(android.R.integer.config_shortAnimTime), 1);

        int resource = getArguments().getInt("resource");

        XmlResourceParser parser = getResources().getXml(resource);
        try {
            int eventType = parser.getEventType();
            do {
                if (eventType == parser.START_DOCUMENT) {
                } else if (eventType == parser.END_DOCUMENT) {
                } else if (eventType == parser.START_TAG) {
                    if (DEBUG) Log.i(TAG, "start: " + parser.getName());
                    String tag = parser.getName();
                    if ("Wizard".equals(tag)) {
                        final TypedArray ta = getActivity().obtainStyledAttributes(parser, R.styleable.WizardDefinition);
                        try {
                            width = ta.getDimensionPixelSize(
                                    R.styleable.WizardDefinition_width,
                                    width);
                            height = ta.getDimensionPixelSize(
                                    R.styleable.WizardDefinition_height,
                                    height);
                            light = ta.getBoolean(R.styleable.WizardDefinition_light, true);
                            background = ta.getDrawable(R.styleable.WizardDefinition_background);
                        } finally {
                            ta.recycle();
                        }
                    }
                    if ("Screen".equals(tag)) {
                        Screen screen = new Screen();

                        int titleId = parser.getAttributeResourceValue(nsAndroid, "title", 0);
                        screen.title = titleId != 0 ? getString(titleId) : parser.getAttributeValue(nsApp, "title");

                        int descriptionId = parser.getAttributeResourceValue(nsAndroid, "text", 0);
                        screen.description = descriptionId != 0 ? getString(descriptionId) : parser.getAttributeValue(nsApp, "text");

                        screen.icon = parser.getAttributeResourceValue(nsAndroid, "icon", 0);

                        screen.id = parser.getIdAttributeResourceValue(0);
                        if (DEBUG) Log.i(TAG, "screen: id=" + screen.id + " title=" + screen.title + " description=" + screen.description);
                        screens.add(screen);
                    }
                } else if (eventType == parser.END_TAG) {
                    if (DEBUG) Log.i(TAG, "end: " + parser.getName());
                } else if (eventType == parser.TEXT) {
                    if (DEBUG) Log.i(TAG, "text: " + parser.getText());
                }
                eventType = parser.next();
            } while (eventType != parser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);

        int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getActivity().getResources().getDisplayMetrics().heightPixels;
        width = Math.min(width, screenWidth*9/10);
        height = Math.min(height, screenHeight*9/10);
    }

    Button prevButton;
    Button nextButton;
    Button doneButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.tutorial_title, getString(R.string.app_name)));

        View retval = inflater.inflate(R.layout.wizard, container, false);
        final ViewPager pager = (ViewPager)retval.findViewById(R.id.view_pager);
        doneButton = (Button) retval.findViewById(R.id.done_button);
        if (doneButton != null) {
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        nextButton = (Button) retval.findViewById(R.id.next_button);
        if (nextButton != null) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pager.getCurrentItem() + 1 < pager.getAdapter().getCount())
                        pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            });
        }
        prevButton = (Button) retval.findViewById(R.id.prev_button);
        if (prevButton != null) {
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pager.getCurrentItem() > 0) ;
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
            });
        }
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return screens.size();
            }

            public Object instantiateItem(ViewGroup container, int position) {
                Screen screen = screens.get(position);
                View retval = inflater.inflate(R.layout.wizard_item, container, false);
                retval.setTag(screen);
                ImageView icon = (ImageView) retval.findViewById(R.id.icon);
                if (icon != null) {
                    icon.setImageResource(screen.icon);
                }
                TextView title = (TextView) retval.findViewById(R.id.title);
                if (title != null) {
                    title.setText(screen.title);
                    title.setVisibility(screen.title == null || screen.title.length() == 0 ? View.GONE : View.VISIBLE);
                }
                TextView description = (TextView) retval.findViewById(R.id.description);
                if (description != null) {
                    description.setText(screen.description);
                    description.setVisibility(screen.description == null || screen.description.length() == 0 ? View.GONE : View.VISIBLE);
                }
                container.addView(retval, 0);
                return retval;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                if (position == screens.size() - 1) {
                    // last
                    if (doneButton.getVisibility() != View.VISIBLE)
                        setVisible(nextButton, false, new Runnable() {
                            @Override
                            public void run() {
                                nextButton.setVisibility(View.GONE);
                                doneButton.setVisibility(View.VISIBLE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                                    doneButton.setAlpha(0.0f);
                                setVisible(doneButton, true, null);
                            }
                        });
                } else {
                    if (nextButton.getVisibility() != View.VISIBLE)
                        setVisible(doneButton, false, new Runnable() {
                            @Override
                            public void run() {
                                doneButton.setVisibility(View.GONE);
                                nextButton.setVisibility(View.VISIBLE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                                    nextButton.setAlpha(0.0f);
                                setVisible(nextButton, true, null);
                            }
                        });
                }
                setVisible(prevButton, position > 0, null);
                prevButton.setEnabled(position > 0);
            }
        });
        retval.setBackgroundDrawable(background);
        return retval;
    }

    @Override
    public void onStart() {
        super.onStart();
        //getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setLayout(width, height);
    }


    private void setVisible(@NonNull final View view, boolean visible, final Runnable cont) {
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            if (cont != null)
                cont.run();
            return;
        }
        view.animate().cancel();
        if (!visible) {
            view.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                boolean cancel = false;
                @Override
                public void onAnimationCancel(Animator animation) {
                    cancel = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (cancel)
                        return;
                    view.setVisibility(View.INVISIBLE);
                    if (cont != null)
                        cont.run();
                }
            });
        } else {
            view.setVisibility(View.VISIBLE);
            view.animate().alpha(1.0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                boolean cancel = false;
                @Override
                public void onAnimationCancel(Animator animation) {
                    cancel = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (cancel)
                        return;
                    if (cont != null)
                        cont.run();
                }
            });
        }
    }
}
