package com.example.android.bookfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String LOG_TAG = MainActivity.class.getSimpleName();

  private ImageView mImageThumbnail;
  private TextView mTextTitle;
  private TextView mTextAuthor;
  private TextView mTextRating;
  private TextView mTextLanguage;
  private TextView mTextDescription;
  private Button mButtonBuy;
  private Button mButtonPreview;
  private String mBuyingLink;
  private String mPreviewLink;
  private String mPublishedDate;
  private Book mSelectedBook;

  @Override protected void onCreate(Bundle savedInstanceState) {

    int listPosition;

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book_details);

    /* Get Intent Extras */
    if (getIntent().getExtras() != null) {
      Bundle bundle = getIntent().getExtras();
      listPosition = bundle.getInt("position");

      // Get the book that was clicked on in Book List
      mSelectedBook = BookListActivity.mListBook.get(listPosition);
    }

    /* initializing the UI elements*/
    mImageThumbnail = (ImageView) findViewById(R.id.text_thumbnail);
    mTextTitle = (TextView) findViewById(R.id.text_title);
    mTextAuthor = (TextView) findViewById(R.id.text_author);
    mTextRating = (TextView) findViewById(R.id.text_rating);
    mTextLanguage = (TextView) findViewById(R.id.text_language);
    mTextDescription = (TextView) findViewById(R.id.text_description);
    mButtonBuy = (Button) findViewById(R.id.text_buy);
    mButtonPreview = (Button) findViewById(R.id.text_preview);

    /* Setting click listener for the buttons */
    mButtonBuy.setOnClickListener(this);
    mButtonPreview.setOnClickListener(this);

    /* Calling method to display books */
    displayBookDetails();
  }

  /* Supporting back button in action bar */
  @Override public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.text_buy:
        launchBuyingLink();
        break;
      case R.id.text_preview:
        launchPreviewLink();
        break;
    }
  }

  /* Method to display the UI elements*/
  public void displayBookDetails() {
    displayImage();
    displayTitle();
    displayAuthor();
    displayRating();
    displayLanguage();
    displayDescription();
    setBuyingLink();
    displayDate();
    setPreviewLink();
    displayPageCount();
    displayPrintType();
    displayCategories();
    displayPdfStatus();
    displayEpubStatus();
  }

  /* Method to display thumbnail image of the book*/
  public void displayImage() {
    String image = mSelectedBook.getThumbnailLink();

    if (image != null && image.length() > 0) {
      Picasso.with(this).load(image).into(mImageThumbnail);
    } else {
      Picasso.with(this).load(R.drawable.image_not_found).into(mImageThumbnail);
    }
  }

  /* Method to display title of the book*/
  public void displayTitle() {
    mTextTitle.setText(mSelectedBook.getTitle());
  }

  /* Method to display author of the book*/
  public void displayAuthor() {
    String author = mSelectedBook.getAuthor();

    if (author != null && author.length() > 0) {
      mTextAuthor.setText(author);
    } else {
      mTextAuthor.setText(getString(R.string.label_no_author));
    }
  }

  /* Method to display rating of the book*/
  public void displayRating() {
    double rating = mSelectedBook.getRating();
    String output;

    if (rating != 0) {
      output = String.format(getString(R.string.label_rating), rating);
    } else {
      output = getString(R.string.label_no_rating);
    }
    mTextRating.setText(output);
  }

  /* Method to display supported language of the book*/
  public void displayLanguage() {
    String language = mSelectedBook.getLanguage();
    if (language != null && language.length() > 0) {
      language = String.format(getString(R.string.label_language), language);
    } else {
      language = String.format(getString(R.string.label_no_language));
    }
    mTextLanguage.setText(language);
  }

  /* Method to display description for the book*/
  public void displayDescription() {
    String description = mSelectedBook.getDescription();
    if (description != null && description.length() > 0) {
      mTextDescription.setText(description);
    } else {
      mTextDescription.setText(getString(R.string.label_no_desc));
    }
  }

  /* Method to set buy button if buying link available for the book*/
  public void setBuyingLink() {
    mBuyingLink = mSelectedBook.getBuyingLink();

    if (mBuyingLink != null && mBuyingLink.length() == 0) {
      mButtonBuy.setVisibility(View.GONE);
    }
  }

  /* Method to set preview button if preview link is available for the book*/
  public void setPreviewLink() {
    mPreviewLink = mSelectedBook.getPreviewlLink();

    if (mPreviewLink != null && mPreviewLink.length() == 0) {
      mButtonPreview.setVisibility(View.GONE);
    }
  }

  /* Method to display buying link for the book*/
  public void launchBuyingLink() {
    Uri bookBuyingURL = Uri.parse(mBuyingLink);
    Intent webIntent = new Intent(Intent.ACTION_VIEW, bookBuyingURL);
    startActivity(webIntent);
  }

  /* Method to display preview link for the book*/
  public void launchPreviewLink() {
    Uri bookPreviewURL = Uri.parse(mPreviewLink);
    Intent webIntent = new Intent(Intent.ACTION_VIEW, bookPreviewURL);
    startActivity(webIntent);
  }

  /* Method to format and display published date for the book*/
  public void displayDate() {
    String date = mSelectedBook.getPublishedDate();
    String dateComplete = "";
    String dateYearOnly = "";
    mPublishedDate = "";
    TextView dateView = (TextView) findViewById(R.id.text_published_date);

    if (date != null && date.length() != 0) {
      if (date.length() == 4) {// check if date is YYYY
        dateYearOnly = date;
      } else if (date.length() == 7 || date.length() == 10) { // check if date is YYYY-MM-DD
        dateComplete = date;
      } else if (date.length() > 10) {
        dateComplete = date.substring(0, 10);
      }

      String finalizedDate = (dateComplete != "") ? dateComplete : dateYearOnly;
      SimpleDateFormat newFormat =
          (dateComplete != "") ? (new SimpleDateFormat("MMM yyyy", Locale.ENGLISH))
              : (new SimpleDateFormat("yyyy", Locale.ENGLISH));
      SimpleDateFormat dateFormat =
          (dateComplete != "") ? (new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH))
              : (new SimpleDateFormat("yyyy", Locale.ENGLISH));

      try {
        Date dt = dateFormat.parse(finalizedDate);
        mPublishedDate = newFormat.format(dt);

        if (mPublishedDate != null && mPublishedDate.length() > 0) {
          mPublishedDate =
              String.format(getApplicationContext().getResources().getString(R.string.label_date),
                  mPublishedDate);
          dateView.setText(mPublishedDate);
        }
      } catch (ParseException pe) {
        Log.e(LOG_TAG, this.getString(R.string.exception_date_format), pe);
      }
    } else {
      mPublishedDate = getApplicationContext().getResources().getString(R.string.label_no_date);
      dateView.setText(mPublishedDate);
    }
  }

  /* Method to display page count for the book*/
  public void displayPageCount() {
    Integer count = mSelectedBook.getPageCount();
    String output;
    TextView pageCountView = (TextView) findViewById(R.id.text_page_count);

    if (count != 0) {
      output =
          String.format(getApplicationContext().getResources().getString(R.string.label_page_count),
              count);
    } else {
      output = getApplicationContext().getResources().getString(R.string.label_no_page_count);
    }
    pageCountView.setText(output);
  }

  /* Method to display print type for the book*/
  public void displayPrintType() {
    String type = mSelectedBook.getPrintType();
    String output;
    TextView printTypeView = (TextView) findViewById(R.id.text_print_type);

    if (type != null && type.length() > 0) {
      output =
          String.format(getApplicationContext().getResources().getString(R.string.label_print_type),
              type);
    } else {
      output = getApplicationContext().getResources().getString(R.string.label_no_print_type);
    }
    printTypeView.setText(output);
  }

  /* Method to display type of category for the book*/
  public void displayCategories() {
    String categories = mSelectedBook.getCategories();
    String output;
    TextView categoryView = (TextView) findViewById(R.id.text_categories);

    if (categories != null && categories.length() > 0) {
      output =
          String.format(getApplicationContext().getResources().getString(R.string.label_category),
              categories);
    } else {
      output = getApplicationContext().getResources().getString(R.string.label_no_category);
    }
    categoryView.setText(output);
  }

  /* Method to display EPUB status for the book*/
  public void displayEpubStatus() {
    Boolean epubStatus = mSelectedBook.getEpubStatus();
    String output;
    TextView epubView = (TextView) findViewById(R.id.text_epub);
    if (mSelectedBook.getEpubStatus()) {
      output = getApplicationContext().getResources().getString(R.string.label_epub_status);
    } else {
      output = getApplicationContext().getResources().getString(R.string.label_no_epub_status);
    }
    epubView.setText(output);
  }

  /* Method to display PDF status for the book*/
  public void displayPdfStatus() {
    Boolean pdfStatus = mSelectedBook.getPdfStatus();
    String output;
    TextView pdfView = (TextView) findViewById(R.id.text_pdf);
    if (mSelectedBook.getPdfStatus()) {
      output = getApplicationContext().getResources().getString(R.string.label_pdf_status);
    } else {
      output = getApplicationContext().getResources().getString(R.string.label_no_pdf_status);
    }
    pdfView.setText(output);
  }
}

