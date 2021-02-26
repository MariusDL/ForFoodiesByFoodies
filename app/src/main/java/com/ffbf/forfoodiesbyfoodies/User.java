package com.ffbf.forfoodiesbyfoodies;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
        private String first_name;
        private String last_name;
        private String profile_image ="";
        private String user_id;
        private String email;
        private String critic;
        private int votes =0;
        private boolean admin = false;

        public User(){}
        //constructor
        public User(String first_name, String last_name, String user_id, String email, String critic) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.user_id = user_id;
            this.email = email;
            this.critic = critic;
        }
        //parcelable has been implemented to send user objects between activities
        protected User(Parcel in){
            first_name = in.readString();
            last_name = in.readString();
            profile_image = in.readString();
            user_id = in.readString();
            email = in.readString();
            critic= in.readString();
            votes = in.readInt();
            admin = in.readByte() != 0;
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel parcel) {
                return new User(parcel);
            }

            @Override
            public User[] newArray(int i) {
                return new User[i];
            }
        };

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(first_name);
            parcel.writeString(last_name);
            parcel.writeString(profile_image);
            parcel.writeString(user_id);
            parcel.writeString(email);
            parcel.writeString(critic);
            parcel.writeInt(votes);
            parcel.writeByte((byte) (admin ? 1:0));
    }

        @Override
        public int describeContents() {
            return 0;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public int getVotes() {
            return votes;
        }
        public void setVotes(int votes) {
            this.votes = votes;
        }
        public boolean isAdmin() {
            return admin;
        }
        public void setAdmin(boolean admin) {
            this.admin = admin;
        }
        public String getUser_id() {
            return user_id;
        }
        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
        public String getCritic() {
            return critic;
        }
        public void setCritic(String critic) {
            this.critic = critic;
        }
        public String getFirst_name() {
            return first_name;
        }
        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }
        public String getLast_name() {
            return last_name;
        }
        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }
        public String getProfile_image() {
            return profile_image;
        }
        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }
    }
