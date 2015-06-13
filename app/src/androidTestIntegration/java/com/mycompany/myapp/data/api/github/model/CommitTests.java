package com.mycompany.myapp.data.api.github.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import com.mycompany.myapp.data.api.github.model.Commit.Author;
import com.mycompany.myapp.data.api.github.model.Commit.CommitDetails;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

@RunWith(AndroidJUnit4.class)
public class CommitTests {
    /**
     * Verification that Parceler is working. The default configuration doesn't play well with other
     * generators and the code gen task wasn't running (see: https://github.com/johncarl81/parceler/issues/24#issuecomment-37925333)
     */
    @Test
    public void testWriteToParcel() {
        Commit commit = new Commit();
        commit.commit = new CommitDetails();
        commit.commit.author = new Author();
        commit.commit.author.name = "Author";
        commit.commit.message = "Message";
        Parcelable parcelable = Parcels.wrap(commit);

        // Write the parcel out to bytes and read it back in
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeParcelable(parcelable, 0);
            parcel.setDataPosition(0);
            parcelable = parcel.readParcelable(parcelable.getClass().getClassLoader());
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }

        Commit unwrapped = Parcels.unwrap(parcelable);
        Assert.assertNotSame(commit, unwrapped);
        Assert.assertEquals("Author", unwrapped.commit.author.name);
        Assert.assertEquals("Message", unwrapped.commit.message);
    }
}
