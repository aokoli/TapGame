/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package com.okolialex.tapgame.android;

public final class R {
    public static final class attr {
        /** <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int tileSize=0x7f010000;
    }
    public static final class color {
        public static final int black=0x7f040001;
        public static final int text_violet=0x7f040000;
    }
    public static final class dimen {
        /**  Default screen margins, per the Android Design guidelines. 

         Customize dimensions originally defined in res/values/dimens.xml (such as
         screen margins) for sw720dp devices (e.g. 10" tablets) in landscape here.
    
         */
        public static final int activity_horizontal_margin=0x7f050000;
        public static final int activity_vertical_margin=0x7f050001;
    }
    public static final class drawable {
        public static final int brick=0x7f020000;
        public static final int brick2=0x7f020001;
        public static final int greenstar=0x7f020002;
        public static final int ic_launcher=0x7f020003;
        public static final int lib_bg=0x7f020004;
        public static final int lib_circle=0x7f020005;
        public static final int lib_cross=0x7f020006;
        public static final int meta=0x7f020007;
        public static final int meta_480dp=0x7f020008;
        public static final int monster_block=0x7f020009;
        public static final int monster_nonblock=0x7f02000a;
        public static final int monstp=0x7f02000b;
        public static final int redstar=0x7f02000c;
        public static final int yellowstar=0x7f02000d;
    }
    public static final class id {
        public static final int action_settings=0x7f090009;
        public static final int button_hard_game=0x7f090004;
        public static final int button_normal_game=0x7f090003;
        public static final int editText1=0x7f090001;
        public static final int game_view=0x7f090000;
        public static final int gamegrid=0x7f090005;
        public static final int score_HUD=0x7f090007;
        public static final int textView1=0x7f090002;
        public static final int time_HUD=0x7f090008;
        public static final int user_prompt_HUD=0x7f090006;
    }
    public static final class layout {
        public static final int activity_main=0x7f030000;
        public static final int intro_screen=0x7f030001;
        public static final int tapgame_layout=0x7f030002;
    }
    public static final class menu {
        public static final int intro_screen=0x7f080000;
        public static final int main=0x7f080001;
    }
    public static final class string {
        public static final int action_settings=0x7f060001;
        public static final int app_name=0x7f060000;
        public static final int hard_game_prompt=0x7f060008;
        public static final int intro_message=0x7f060006;
        public static final int normal_game_prompt=0x7f060007;
        public static final int press_screen_text=0x7f060002;
        public static final int score_text=0x7f060003;
        public static final int time_text=0x7f060004;
        public static final int title_activity_intro_screen=0x7f060005;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.
    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.
        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.
    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f070000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f070001;
    }
    public static final class styleable {
        /** Attributes that can be used with a TileView.
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #TileView_tileSize com.okolialex.tapgame.android:tileSize}</code></td><td></td></tr>
           </table>
           @see #TileView_tileSize
         */
        public static final int[] TileView = {
            0x7f010000
        };
        /**
          <p>This symbol is the offset where the {@link com.okolialex.tapgame.android.R.attr#tileSize}
          attribute's value can be found in the {@link #TileView} array.


          <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name com.okolialex.tapgame.android:tileSize
        */
        public static final int TileView_tileSize = 0;
    };
}