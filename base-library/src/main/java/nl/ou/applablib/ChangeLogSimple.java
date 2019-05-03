/**
 * Copyright (C) 2011, Karsten Priegnitz
 *
 * Permission to use, copy, modify, and distribute this piece of software
 * for any purpose with or without fee is hereby granted, provided that
 * the above copyright notice and this permission notice appear in the
 * source code of all copies.
 *
 * It would be appreciated if you mention the author in your change log,
 * contributors list or the like.
 *
 * @author: Karsten Priegnitz
 * @see: http://code.google.com/p/android-change-log/
 */
//package sheetrock.panda.changelog;
package nl.ou.applablib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChangeLogSimple {

    @NonNull
    private final Context context;
    private String lastVersion, thisVersion;
    private int res;

    // this is the key for storing the version name in SharedPreferences
    private static final String VERSION_KEY = "PREFS_VERSION_KEY";

    /**
     * Constructor
     *
     * Retrieves the version names and stores the new version name in
     * SharedPreferences
     *
     * @param context
     */
    public ChangeLogSimple(@NonNull Context context, int res, String thisVersion) {
        this(context, res, thisVersion, 0);
    }
    
    public ChangeLogSimple(@NonNull Context context, int res, String thisVersion, int theme) {
    	this(context, thisVersion, PreferenceManager.getDefaultSharedPreferences(context), theme);
    	this.res = res;
    }

    /**
     * Constructor
     *
     * Retrieves the version names and stores the new version name in
     * SharedPreferences
     *
     * @param context
     * @param sp  the shared preferences to store the last version name into
     */
    public ChangeLogSimple(@NonNull Context context, String thisVersion, @NonNull SharedPreferences sp, int theme) {
    	//if (theme == 0 && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
    	//	theme = R.style.Dialog_Fix;
        this.context = theme == 0 ? context : new ContextThemeWrapper(context, theme);

        // get version numbers
        this.lastVersion = sp.getString(VERSION_KEY, "");
        //Log.d(TAG, "lastVersion: " + lastVersion);
        this.thisVersion = thisVersion;
        if (this.thisVersion == null) {
        	try {
        		this.thisVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        	} catch (NameNotFoundException e) {
        		this.thisVersion = "?";
        		Log.e(TAG, "could not get version name from manifest!");
        		e.printStackTrace();
        	}
        }
        //Log.d(TAG, "appVersion: " + this.thisVersion);

        // save new version number to preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VERSION_KEY, this.thisVersion);
        editor.apply();
    }

    /**
     * @return  The version name of the last installation of this app (as
     *          described in the former manifest). This will be the same as
     *          returned by <code>getThisVersion()</code> the second time
     *          this version of the app is launched (more precisely: the
     *          second time ChangeLog is instantiated).
     * @see AndroidManifest.xml#android:versionName
     */
    public String getLastVersion() {
        return  this.lastVersion;
    }

    /**
     * @return  The version name of this app as described in the manifest.
     * @see AndroidManifest.xml#android:versionName
     */
    public String getThisVersion() {
        return  this.thisVersion;
    }

    /**
     * @return  <code>true</code> if this version of your app is started the
     *          first time
     */
    public boolean firstRun() {
        return  ! this.lastVersion.equals(this.thisVersion);
    }

    /**
     * @return  <code>true</code> if your app is started the first time ever.
     *          Also <code>true</code> if your app was deinstalled and
     *          installed again.
     */
    public boolean firstRunEver() {
        return  "".equals(this.lastVersion);
    }

    /**
     * @return  an AlertDialog displaying the changes since the previous
     *          installed version of your app (what's new).
     */
    @NonNull
    public AlertDialog getLogDialog(int extra) {
        return this.getDialog(false, extra);
    }

    /**
     * @return  an AlertDialog with a full change log displayed
     */
    @NonNull
    public AlertDialog getFullLogDialog() {
        return  this.getDialog(true, Integer.MAX_VALUE);
    }

    @NonNull
    private AlertDialog getDialog(boolean full, int extra) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        Pair<String,ArrayList<Pair<CharacterStyle,Pair<Integer,Integer>>>> log = getLog(full, extra);
        TextView tv = new TextView(builder.getContext());
        /*
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
            tv.setTextColor(Color.WHITE); //context.getResources().getColor(android.R.color.primary_text_light));
        }
        */
        //tv.setTextColor(context.getResources().getColor(R.attr.textColorPrimary));
        /*
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
	        TypedValue typedValue = new TypedValue();
	        if (context.getTheme().resolveAttribute(android.R.attr.textColorPrimaryInverse, typedValue, true))
	            tv.setTextColor(context.getResources().getColor(typedValue.resourceId));
        } else {
        	TypedValue typedValue = new TypedValue();
	        if (context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true))
	            tv.setTextColor(context.getResources().getColor(typedValue.resourceId));
        }
        */
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[] { R.attr.dialogPreferredPadding };
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int paddingDialog = a.getDimensionPixelSize(0, 0);
        a.recycle();

        tv.setPadding(paddingDialog, 0, paddingDialog, 0);

        tv.setText(log.first, TextView.BufferType.SPANNABLE);
        /*
        WebView wv = new WebView(this.context);
        wv.setBackgroundColor(0); // transparent
        // wv.getSettings().setDefaultTextEncodingName("utf-8");
        wv.loadDataWithBaseURL(null, this.getLog(full), "text/html", "UTF-8", null);
        */
        Spannable span = (Spannable)tv.getText();
        for (Pair<CharacterStyle,Pair<Integer,Integer>> s : log.second) {
            span.setSpan(s.first, s.second.first.intValue(), s.second.second.intValue(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //tv.setInputType(InputType.TYPE_NULL);
        //tv.setEnabled(false);
        //tv.setFocusable(false);
        
        ScrollView sv = new ScrollView(builder.getContext());
        sv.addView(tv);
        sv.setPadding(0, paddingDialog/2, 0, 0);

        builder.setTitle(context.getResources().getString(full
                         ? R.string.changelog_full_title
                         : R.string.changelog_title))
        .setView(sv)
        .setCancelable(true)
        .setNegativeButton(context.getResources().getString(R.string.changelog_ok_button),
            new DialogInterface.OnClickListener() {
            @Override
			public void onClick(@NonNull DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return  builder.create();
    }

    /**
     * @return  HTML displaying the changes since the previous
     *          installed version of your app (what's new)
     */
    /*
    public String getLog() {
        return  this.getLog(false);
    }
    */

    /**
     * @return  HTML which displays full change log
     */
    /*
    public String getFullLog() {
        return  this.getLog(true);
    }
    */

    /** modes for HTML-Lists (bullet, numbered) */

    @NonNull
    private Pair<String,ArrayList<Pair<CharacterStyle,Pair<Integer,Integer>>>> getLog(boolean full, int extra) {
        // read changelog.txt file
        boolean active = false;
        StringBuffer sb = new StringBuffer();
        ArrayList<Pair<CharacterStyle,Pair<Integer,Integer>>> styles = new ArrayList<Pair<CharacterStyle,Pair<Integer,Integer>>>();
        try {
            InputStream ins = context.getResources().openRawResource(res);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                char marker = line.length() > 0 ? line.charAt(0) : 0;
                if (marker == '$') {
                    // begin of a version section
                    String version = line.substring(1).trim();
                    // stop output?
                    active = full || version.equals(thisVersion) || (active && extra-- > 0);
                    if (sb.length() > 0)
                        sb.append("\n");
                } else {
                    switch (marker) {
                    case '%':
                        // line contains version title
                        if (active) {
                            int begin = sb.length();
                            sb.append(line.substring(1).trim()).append("\n");
                            int end = sb.length();
                            styles.add(new Pair<CharacterStyle, Pair<Integer,Integer>>(new StyleSpan(android.graphics.Typeface.BOLD), new Pair<Integer,Integer>(begin, end)));
                        }
                        break;
                    case '#':
                    	// comment
                    	break;
                    case '*':
                        // line contains bullet list item
                        if (active) {
                            sb.append(((char)183));
                            sb.append(" ").append(line.substring(1).trim()).append("\n");
                        }
                        break;
                    default:
                        // no special character: just use line as is
                        if (active)
                            sb.append(line).append("\n");
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<String,ArrayList<Pair<CharacterStyle,Pair<Integer,Integer>>>>(sb.toString().trim(), styles);
    }

    private static final String TAG = "ChangeLog";

    /**
     * manually set the last version name - for testing purposes only
     * @param lastVersion
     */
    void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }
}
