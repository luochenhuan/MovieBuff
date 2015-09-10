# MovieBuff
``MovieBuff`` shows trending movies from [The Movie Database](https://www.themoviedb.org/?language=en). It uses [The Movie Database API](https://www.themoviedb.org/documentation/api) to fetch movie information.

The whole UI design follows Google's [Material Design](https://developer.android.com/design/index.html) principles.


## What to Expect
With ``MovieBuff`` app, you can:
* Discover the most popular or the highest rated movies

## Screenshots


## Key Tech Features
* Use Sharedpreferences to store/restore sort request parameter
* Use of Android Design Support Library: 
  - Navigation Drawer
  - CardView
  - CoordinatorLayout
  - Toolbar
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
* [ExpandableTextView](https://github.com/Manabu-GT/ExpandableTextView)
