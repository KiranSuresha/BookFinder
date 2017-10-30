package com.example.android.bookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

  private static final String LOG_TAG = BookAdapter.class.getName();

  /**
   * Constructor to create a new {@link BookAdapter} object
   */
  public BookAdapter(Context context, List<Book> books) {
    super(context, 0, books);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    // Check if an existing view is being reused, otherwise inflate the view
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView =
          LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
    }

    /** Getting current book position in the list */
    Book currentBook = getItem(position);

    /** Setting title for the book */
    TextView textViewTitle = listItemView.findViewById(R.id.text_book_title);
    textViewTitle.setText(currentBook.getTitle());

    /** Setting author for the book */
    TextView textViewAuthor = listItemView.findViewById(R.id.text_book_author);
    textViewAuthor.setText(currentBook.getAuthor());

    /** Setting language for the book */
    TextView textViewLanguage = listItemView.findViewById(R.id.text_book_language);
    textViewLanguage.setText(currentBook.getLanguage());

    /** Setting thumbnail image for the book */
    String image = currentBook.getThumbnailLink();
    ImageView mImageThumbnail = listItemView.findViewById(R.id.text_book_image);

    if (image != null && image.length() > 0) {
      Picasso.with(getContext()).load(image).into(mImageThumbnail);
    } else {
      Picasso.with(getContext()).load(R.drawable.image_not_found).into(mImageThumbnail);
    }

    /** Setting rating for the book */
    Double ratingTextDouble = currentBook.getRating();
    String output;
    TextView textViewRating = listItemView.findViewById(R.id.text_book_rating);

    if (ratingTextDouble != 0) {
      output = String.format(getContext().getResources().getString(R.string.label_rating),
          ratingTextDouble);
    } else {
      output = getContext().getResources().getString(R.string.label_no_rating);
    }
    textViewRating.setText(output);

    return listItemView;
  }
}
