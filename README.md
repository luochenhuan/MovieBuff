# MovieBuff
``MovieBuff`` shows trending movies from [The Movie Database](https://www.themoviedb.org/?language=en). It uses [The Movie Database API](https://www.themoviedb.org/documentation/api) to fetch movie information.

The whole UI design follows Google's [Material Design](https://developer.android.com/design/index.html) principles.


## What to Expect
With ``MovieBuff`` app, you can:
* Discover the most popular or the highest rated movies

## Screenshots
* Navigation Drawer and [Hamburger Animation](https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer#animate-the-hamburger-icon):
  - ![](http://i98.photobucket.com/albums/l265/Haiyu_Zhen/Android%20Screen%20Shots/ezgif.com-video-to-gif_zpsgbzgljml.gif)
  - ![](https://camo.githubusercontent.com/0b3031ced336e7fa80d7eb211e60b1b924d20152/687474703a2f2f696d6775722e636f6d2f656b6d576c37712e676966)

* sort preference change from popularity to average voting:
![sort_change](http://i98.photobucket.com/albums/l265/Haiyu_Zhen/Android%20Screen%20Shots/sort_change_zpsxvvyviwl.gif)

* Snackbar shows when network connection fails:
![Snackbar](http://i98.photobucket.com/albums/l265/Haiyu_Zhen/Android%20Screen%20Shots/snack_zpskuz05ozf.png)


## Major Tech Features
I have been struggling for achieving a better UX and use as many native Android widgets as possible. I need MORE COFFEE!

* Use Sharedpreferences to store/restore sort request parameter
* Use AsyncTask in a background thread for http requests
* RecyclerView and customized adapter
* Activity/Fragemnt states saved to handle Configuration Changes(orientation change)
* Use of Android Design Support Library: 
  - Navigation Drawer
  - CardView
  - CoordinatorLayout
  - Toolbar
  - Snackbar
  - CollapsingToolbarLayout
  - Floating Action Button
  

## How to Use
An API Key is required to use the app. The API key is in ``res/values/strings.xml``:
    ```
    <string name="api_key">YOUR_API_KEY</string>
    ```

The target sdk version is **23**, and the min sdk version is **15**.

## Third-party Libraries
* [Picasso](http://square.github.io/picasso/)
* [butterknife](https://github.com/JakeWharton/butterknife)
* [ExpandableTextView](https://github.com/Manabu-GT/ExpandableTextView)
* [FABRevealLayout](https://github.com/truizlop/FABRevealLayout)
* [okhttp](https://github.com/square/okhttp)

## Resources that I found truly helpful and saved my ass:
- [wiki of how to set up nav layout & action bar](https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer)
- [tutorial on using Design Support Library](http://inthecheesefactory.com/blog/android-design-support-library-codelab/en)
