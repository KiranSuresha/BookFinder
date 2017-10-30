package com.example.android.bookfinder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper methods related to requesting and receiving book data from Google API
 */

public final class QueryUtils {

  private static final String LOG_TAG = QueryUtils.class.getName();

  // Google Book API Keys
  private static final String KEY_ITEMS = "items";
  private static final String KEY_VOLUMEINFO = "volumeInfo";
  private static final String KEY_TITLE = "title";
  private static final String KEY_AUTHORS = "authors";
  private static final String KEY_PUBLISHED_DATE = "publishedDate";
  private static final String KEY_CATEGORIES = "categories";
  private static final String KEY_PAGECOUNT = "pageCount";
  private static final String KEY_LANGUAGE = "language";
  private static final String KEY_PREVIEWLINK = "previewLink";
  private static final String KEY_PRINT_TYPE = "printType";
  private static final String KEY_RATING = "averageRating";
  private static final String KEY_DESCRIPTION = "description";
  private static final String KEY_IMAGELINKS = "imageLinks";
  private static final String KEY_THUMBNAIL = "smallThumbnail";
  private static final String KEY_SALEINFO = "saleInfo";
  private static final String KEY_BUYLINK = "buyLink";
  private static final String KEY_ACCESSINFO = "accessInfo";
  private static final String KEY_EPUB = "epub";
  private static final String KEY_PDF = "pdf";
  private static final String KEY_ISAVAILABLE = "isAvailable";
  private static Context mContext;

  /**
   * This is a private constructor and only meant to hold static variables and methods,
   * which can be accessed directly from the class name Utils
   */
  private QueryUtils() {
  }

  /**
   * Query the URL and return a list of {@link Book} objects.
   */
  public static List<Book> fetchBookData(String requestUrl, Context context) {

    mContext = context;

    // Create URL object
    URL url = createUrl(requestUrl);

    // Perform HTTP request to the URL and receive a JSON response back
    String jsonResponse = null;
    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_http_request), e);
    }

    // Extract relevant fields from the JSON response and create a list of {@link Book}s
    List<Book> books = extractFeatureFromJson(jsonResponse);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Return the list of {@link Book}
    return books;
  }

  /**
   * This method returns new URL object from the given string URL
   */
  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_url_invalid), e);
    }
    return url;
  }

  /**
   * Make an HTTP request to the given URL and return a String as the response.
   */
  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    // If the URL is null, then return early
    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(10000 /* milliseconds */);
      urlConnection.setConnectTimeout(15000 /* milliseconds */);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      // If the request was successful (response code 200),
      // then read the input stream and parse the response.
      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG,
            mContext.getString(R.string.exception_resp_code) + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return jsonResponse;
  }

  /**
   * Convert the {@link InputStream} into a String which contains the
   * whole JSON response from the server.
   */
  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader =
          new InputStreamReader(inputStream, Charset.forName("UTF-8"));
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return output.toString();
  }

  /**
   * Return a list of {@link Book} objects retrieved from parsing a JSON response.
   */
  private static List<Book> extractFeatureFromJson(String bookJSON) {

    /** If the JSON string is empty or null, then return early. */
    if (TextUtils.isEmpty(bookJSON)) {
      return null;
    }

    /** Create an empty ArrayList used to add books */
    List<Book> books = new ArrayList<>();

    try {
      JSONObject baseJsonResponse;        // JSON Object for the data retrieved from API request
      JSONArray bookArray;                // Array of books returned in the JSON object
      JSONObject currentBook;             // Single book at a specific position in the bookArray
      JSONObject volumeInfo;              // VolumeInfo object of the currentBook

      JSONArray authorsArray;             // Author Array of the currentBook

      JSONArray categoriesArray;          // Categories Array of the currentBook

      JSONObject imageLinks;              // JSON Object with various image links

      JSONObject saleInfo;                // JSON Object for Sale Info of the currentBook

      JSONObject accessInfo;              // JSON Object for Access Info of the currentBook
      JSONObject
          epub;                           // JSON Object for epub flag - obtained from accessInfo object
      JSONObject
          pdf;                            // JSON Object for pdf flag - obtained from accessInfo object

      baseJsonResponse = new JSONObject(bookJSON);

      if (baseJsonResponse.has(KEY_ITEMS)) {
        bookArray = baseJsonResponse.getJSONArray(KEY_ITEMS);

        for (int i = 0; i < bookArray.length(); i++) {

          String title;                       // Title of the currentBook
          String authorList =
              "";             // Authors of the currentBook - obtained from authorsArray
          String language = "";               // Language of the currentBook
          String thumbnailLink =
              "";                             // Thumbnail Link of the currentBook - obtained from imageLinks
          String description = "";            // Description of the currentBook
          double rating = 0.0;         // Average Rating of the currentBook
          String publishedDate = "";          // Published Date of the currentBook
          String previewLink = "";            // Preview Link of the currentBook
          String buyLink =
              "";                             // Buying Link of the currentBook - obtained from saleInfo object
          int pageCount = 0;                  // PageCount of the currentBook
          String printType = "";              // Print Type of the currentBook
          String category =
              "";                             // Category of the currentBook - obtained from categoriesArray
          boolean isAvailableEpub = false;    // Indicator for EPUB version availability
          boolean isAvailablePdf = false;     // Indicator for PDF version availability

          currentBook = bookArray.getJSONObject(i);
          volumeInfo = currentBook.getJSONObject(KEY_VOLUMEINFO);
          title = volumeInfo.getString(KEY_TITLE);

          // Get value for author if the key exists
          String author = "";
          if (volumeInfo.has(KEY_AUTHORS)) {
            authorsArray = volumeInfo.getJSONArray(KEY_AUTHORS);

            if (authorsArray.length() > 1) {
              authorList = authorsArray.join(", ").replaceAll("\"", "");
            } else if (authorsArray.length() == 1) {
              authorList = authorsArray.getString(0);
            } else if (authorsArray.length() == 0) {
              authorList = "";
            }
          }

          // Get value for publishedDate if the key exists
          if (volumeInfo.has(KEY_PUBLISHED_DATE)) {
            publishedDate = volumeInfo.getString(KEY_PUBLISHED_DATE);
          } else {
            publishedDate = "";
          }

          // Get value for category if the key exists
          if (volumeInfo.has(KEY_CATEGORIES)) {
            categoriesArray = volumeInfo.getJSONArray(KEY_CATEGORIES);
            category = categoriesArray.getString(0);
          } else {
            category = "";
          }

          // Get value for language if the key exists
          if (volumeInfo.has(KEY_LANGUAGE)) {
            language = volumeInfo.getString(KEY_LANGUAGE);
          } else {
            language = "";
          }

          // Get value for preview link if the key exists
          if (volumeInfo.has(KEY_PREVIEWLINK)) {
            previewLink = volumeInfo.getString(KEY_PREVIEWLINK);
          } else {
            previewLink = "";
          }

          // Get value pageCount if the key exists
          if (volumeInfo.has(KEY_PAGECOUNT)) {
            pageCount = volumeInfo.getInt(KEY_PAGECOUNT);
          } else {
            pageCount = 0;
          }

          // Get value printType if the key exists
          if (volumeInfo.has(KEY_PRINT_TYPE)) {
            printType = volumeInfo.getString(KEY_PRINT_TYPE);
          } else {
            printType = "";
          }

          // Get value for description if the key exists
          if (volumeInfo.has(KEY_DESCRIPTION)) {
            description = volumeInfo.getString(KEY_DESCRIPTION);
          } else {
            description = "";
          }

          // Get value for rating if the key exists
          if (volumeInfo.has(KEY_RATING)) {
            rating = volumeInfo.getDouble(KEY_RATING);
          } else {
            rating = 0.0;
          }

          // Get value for smallThumbnail if the key exists
          if (volumeInfo.has(KEY_IMAGELINKS)) {
            imageLinks = volumeInfo.getJSONObject(KEY_IMAGELINKS);
            if (imageLinks.has(KEY_THUMBNAIL)) {
              thumbnailLink = imageLinks.getString(KEY_THUMBNAIL);
            } else {
              thumbnailLink = null;
            }
          }

          // Get buying link if the key exist
          if (currentBook.has(KEY_SALEINFO)) {
            saleInfo = currentBook.getJSONObject(KEY_SALEINFO);

            if (saleInfo.has(KEY_BUYLINK)) {
              buyLink = saleInfo.getString(KEY_BUYLINK);
            } else {
              buyLink = "";
            }
          }

          // Get indicators if EPUB and PDF versions available
          if (currentBook.has(KEY_ACCESSINFO)) {
            accessInfo = currentBook.getJSONObject(KEY_ACCESSINFO);

            if (accessInfo.has(KEY_EPUB)) {
              epub = accessInfo.getJSONObject(KEY_EPUB);
              isAvailableEpub = epub.getBoolean(KEY_ISAVAILABLE);
            } else {
              isAvailableEpub = false;
            }

            if (accessInfo.has(KEY_PDF)) {
              pdf = accessInfo.getJSONObject(KEY_PDF);
              isAvailablePdf = pdf.getBoolean(KEY_ISAVAILABLE);
            } else {
              isAvailablePdf = false;
            }
          }

          // Create a new {@link Book} object with parameters obtained from JSON response
          Log.d(LOG_TAG, "thumbnail link: " + thumbnailLink);
          Book book =
              new Book(title, authorList, language, buyLink, rating, description, thumbnailLink,
                  previewLink, publishedDate, pageCount, printType, category, isAvailableEpub,
                  isAvailablePdf);

          // Add the new {@link Book} object to the list of books
          books.add(book);
        }
      }
    } catch (JSONException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
    }

    // Return the list of books
    return books;
  }
}
