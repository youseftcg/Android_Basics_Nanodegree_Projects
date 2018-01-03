package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.ProductContract.ProductEntry;

import java.io.ByteArrayOutputStream;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.android.inventoryapp.MainActivity.LOG_TAG;
import static com.example.android.inventoryapp.R.id.price;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    int quantity = 0;
    String photoPathFromGallery, photoPathFromDb;
    Button plusBtn, minusBtn;
    TextView quantityHolder, quantityEditor;
    ImageView imageViewBtn;
    Uri mCurrentUri, picFromDb;
    EditText nameEditor, priceEditor;
    boolean dataChange = false;
    private static int PICK_PHOTO_REQUEST = 1;

    View.OnClickListener plusOrMinus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.plus_btn:
                    if (quantity == 0) {
                        minusBtn.setVisibility(View.VISIBLE);
                        quantity++;
                    } else {
                        quantity++;

                    }
                    break;
                case R.id.minus_btn:
                    if (quantity == 1) {
                        quantity--;
                        minusBtn.setVisibility(View.INVISIBLE);
                    } else {
                        quantity--;
                    }

                    break;
            }
            quantityHolder.setText(String.valueOf(quantity));
        }
    };

    View.OnTouchListener mOnTouchListner = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dataChange = true;
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        mCurrentUri = getIntent().getData();

        if (mCurrentUri == null) {
            setTitle("Add a product");
        } else {
            setTitle("Update product");
            getSupportLoaderManager().initLoader(2, null, this);
        }

        imageViewBtn = (ImageView) findViewById(R.id.add_image);
        nameEditor = (EditText) findViewById(R.id.name);
        quantityEditor = (TextView) findViewById(R.id.quantity);
        priceEditor = (EditText) findViewById(price);

        plusBtn = (Button) findViewById(R.id.plus_btn);
        minusBtn = (Button) findViewById(R.id.minus_btn);
        quantityHolder = (TextView) findViewById(R.id.quantity);

        plusBtn.setOnClickListener(plusOrMinus);
        minusBtn.setOnClickListener(plusOrMinus);

        minusBtn.setVisibility(View.INVISIBLE);
        imageViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO_REQUEST);
            }
        });

        imageViewBtn.setOnTouchListener(mOnTouchListner);
        nameEditor.setOnTouchListener(mOnTouchListner);
        plusBtn.setOnTouchListener(mOnTouchListner);
        minusBtn.setOnTouchListener(mOnTouchListner);
        priceEditor.setOnTouchListener(mOnTouchListner);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
        if(mCurrentUri == null){
           MenuItem menuItem =  menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveProduct();
                return true;
            case R.id.delete:

                deleteProduct();
                return true;
            case android.R.id.home:
                if(dataChange){
                    DialogInterface.OnClickListener dialogOnClick = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavUtils.navigateUpFromSameTask(EditorActivity.this);
                        }
                    };
                    showUnSavedChangeDialog(dialogOnClick);
                    return true;
                }
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void saveProduct() {
        // Getting holders, knowing that the ImageView have been declared alreadu before


        //Getting data inside the views
//        Bitmap outBitmap = ((BitmapDrawable) imageViewBtn.getDrawable()).getBitmap();
//        byte[] imageCompressed = compressBitmap(outBitmap);
        String name = nameEditor.getText().toString().trim();
        Log.e(LOG_TAG, "Got Name" + name);
        int quantityOnSave = Integer.parseInt( quantityEditor.getText().toString());
        Log.e(LOG_TAG, "Got quantity" + quantityOnSave);
        String priceString = priceEditor.getText().toString();
        Log.e(LOG_TAG, "Got price" + priceString);



        if(mCurrentUri == null && TextUtils.isEmpty(photoPathFromGallery) &&
                TextUtils.isEmpty(name) && quantityOnSave == 0 &&
                TextUtils.isEmpty(priceString)){
            Log.e(LOG_TAG, "Problem in if");
            finish();
            return ;
        } else if( TextUtils.isEmpty(name) || TextUtils.isEmpty(priceString)){
            Toast.makeText(this, "Fill up all the fields", Toast.LENGTH_SHORT).show();
            return ;
        }
        int priceOnSave = Integer.parseInt(priceString);
        ContentValues contentValues = new ContentValues();

        if (photoPathFromGallery != null || photoPathFromDb != null ) {
            if(photoPathFromGallery != null){
                contentValues.put(ProductEntry.COLUMN_PIC, photoPathFromGallery);
            } else {
                contentValues.put(ProductEntry.COLUMN_PIC, photoPathFromDb);
            }
        }
        contentValues.put(ProductEntry.COLUMN_NAME, name);
        contentValues.put(ProductEntry.COLUMN_QUANTITY, quantityOnSave);
        contentValues.put(ProductEntry.COLUMN_PRICE, priceOnSave);

        if(mCurrentUri == null){
            Uri i = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        } else {
            getContentResolver().update(mCurrentUri, contentValues, null, null);
        }


        finish();
        Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
    }

    private void deleteProduct(){
        if(mCurrentUri != null){
            getContentResolver().delete(mCurrentUri, null, null);
            finish();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int photoColumnIndex = cursor.getColumnIndex(projection[0]);
            photoPathFromGallery = cursor.getString(photoColumnIndex);
            Uri uriPhoto = Uri.parse(photoPathFromGallery);
//            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
//            imageViewBtn.setImageBitmap(bitmap);

            imageViewBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewBtn.setImageURI(uriPhoto);

        }
    }

    private byte[] compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PIC,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE};
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) {
            photoPathFromDb = data.getString(data.getColumnIndex(ProductEntry.COLUMN_PIC));
            if(photoPathFromDb != null){
                picFromDb = Uri.parse(photoPathFromDb);
                imageViewBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewBtn.setImageURI(picFromDb);
            }

            String name = data.getString(data.getColumnIndex(ProductEntry.COLUMN_NAME));
            String quantity = String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_QUANTITY)));
            String price = String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PRICE)));



            nameEditor.setText(name);
            quantityEditor.setText(quantity);
            priceEditor.setText(price);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageViewBtn.setImageURI(null);
        nameEditor.setText("");
        quantityEditor.setText("");
        priceEditor.setText("");
    }

    @Override
    public void onBackPressed() {
        if(!dataChange){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener dialogOnClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        showUnSavedChangeDialog(dialogOnClick);
    }

    private void showUnSavedChangeDialog(DialogInterface.OnClickListener dialogOnClick){
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You haven't saved the new updates, are you sure you wanna discard this?");
        alertDialogBuilder.setPositiveButton("Discard", dialogOnClick);
        alertDialogBuilder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
