package com.example.android.bookfinder;

import android.util.Log;

public class Book {

  public static final String LOG_TAG = Book.class.getSimpleName();

  /** Book Title */
  private String mTitle;

  /** Book Author */
  private String mAuthor;

  /** Language */
  private String mLanguage;

  /** Buying Link */
  private String mBuyingLink;

  /** Rating */
  private double mRating;

  /** Description */
  private String mDescription;

  /** Thumbnail Link */
  private String mThumbnailLink;

  /** Preview Link */
  private String mPreviewlLink;

  /** Published Date */
  private String mPublishedDate;

  /** Page Count */
  private int mPageCount;

  /** Print Type */
  private String mPrintType;

  /** Book Categories */
  private String mCategories;

  /** EPUB Tag */
  private boolean mIsEpubAvailable;

  /** PDF Tag */
  private boolean mIsPdfAvailable;

  /** Constructor */
  public Book(String title, String author, String language, String buyingLink, double rating,
      String description, String thumbnailLink, String previewLink, String publishedDate,
      int pageCount, String printType, String categories, boolean epubAvailable,
      boolean pdfAvailable) {

    /** Initialization */
    mTitle = title;
    mAuthor = author;
    mLanguage = language;
    mBuyingLink = buyingLink;
    mRating = rating;
    mDescription = description;
    mThumbnailLink = thumbnailLink;
    mPreviewlLink = previewLink;
    mPublishedDate = publishedDate;
    mPageCount = pageCount;
    mPrintType = printType;
    mCategories = categories;
    mIsEpubAvailable = epubAvailable;
    mIsPdfAvailable = pdfAvailable;
  }

  /** Getter method - Title */
  public String getTitle() {
    return mTitle;
  }

  /** Getter method - Author */
  public String getAuthor() {
    return mAuthor;
  }

  /** Getter method - Language */
  public String getLanguage() {
    return mLanguage;
  }

  /** Getter method - Rating */
  public double getRating() {
    return mRating;
  }

  /** Getter method - Buying Link */
  public String getBuyingLink() {
    return mBuyingLink;
  }

  /** Getter method - Description */
  public String getDescription() {
    return mDescription;
  }

  /** Getter method - Thumbnail Link */
  public String getThumbnailLink() {
    Log.d(LOG_TAG, "thumbnail link :" + mThumbnailLink);
    return mThumbnailLink;

  }

  /** Getter method - Preview Link */
  public String getPreviewlLink() {
    return mPreviewlLink;
  }

  /** Getter method - Date */
  public String getPublishedDate() {
    return mPublishedDate;
  }

  /** Getter method - Page Count */
  public int getPageCount() {
    return mPageCount;
  }

  /** Getter method - Print Type */
  public String getPrintType() {
    return mPrintType;
  }

  /** Getter method - Categories */
  public String getCategories() {
    return mCategories;
  }

  /** Getter method - EPUB Status */
  public boolean getEpubStatus() {
    return mIsEpubAvailable;
  }

  /** Getter method - PDF Status */
  public boolean getPdfStatus() {
    return mIsPdfAvailable;
  }
}
