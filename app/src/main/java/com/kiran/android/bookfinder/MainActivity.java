package com.kiran.android.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String LOG_TAG = MainActivity.class.getName();

  final Context mContext = this;
  private String mBookTitle;
  private String mBookAuthor;

  // UI Components
  private EditText mEditBookTitle;
  private EditText mEditBookAuthor;
  private FloatingActionButton mButtonSearch;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /* Initialize UI components */
    mEditBookTitle = (EditText) findViewById(R.id.edit_book_title);
    mEditBookAuthor = (EditText) findViewById(R.id.edit_book_author);
    mButtonSearch = (FloatingActionButton) findViewById(R.id.button_search_books);

    /* Set OnClickListner on button view */
    mButtonSearch.setOnClickListener(this);

    /* Set keyboard "ok" button listener**/
    mEditBookAuthor.setOnEditorActionListener(new EditText.OnEditorActionListener() {
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          mButtonSearch.performClick();
          return true;
        }
        return false;
      }
    });
  }

  /**
   * Invokes methods for individual call to action buttons
   */
  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.button_search_books:
        showSearchResults();
        break;
    }
  }

  /**
   * This method launches BookListActivity after validating Book Title and Author inputs
   */
  public void showSearchResults() {
    if (validateInput()) {
      Intent intent = new Intent(mContext, BookListActivity.class);

      // Replace spaces with + sign for URL to be used to fetch data
      intent.putExtra("bookTitle", mBookTitle.replaceAll(" ", "+"));
      intent.putExtra("bookAuthor", mBookAuthor.replaceAll(" ", "+"));
      startActivity(intent);
    }
  }

  /**
   * This method checks if an input string contains number or invalid characters.
   */
  public boolean validateInput() {
    mBookTitle = mEditBookTitle.getText().toString().trim();
    mBookAuthor = mEditBookAuthor.getText().toString().trim();

    /** Check if Book Title is entered; else set error */
    if (mBookTitle.length() == 0) {
      mEditBookTitle.setError(getString(R.string.error_empty_string));
      return false;
    }

    /** Check if Book Author is valid if entered; it is an optional field */
    if (mBookAuthor.length() == 0) {
      return true;
    }
    return true;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.about_menu, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.about:
        // call the about screen
        Intent aboutScreenIntent = new Intent(this, AboutPageActivity.class);
        startActivity(aboutScreenIntent);
        break;
    }
    return true;
  }
}
