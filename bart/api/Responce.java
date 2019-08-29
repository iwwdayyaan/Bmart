package com.bart.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Responce {


    public class UserSignUp {

        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<Data> data = null;

        public Boolean getStatus() {
            return status;
        }


        public String getMessage() {
            return message;
        }


        public List<Data> getData() {
            return data;
        }

    }

    public class Data {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phoneno")
        @Expose
        private String phoneno;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("paasport")
        @Expose
        private String paasport;
        @SerializedName("deviceid")
        @Expose
        private String deviceid;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("isactive")
        @Expose
        private String isactive;

        public String getUserid() {
            return userid;
        }


        public String getName() {
            return name;
        }


        public String getPhoneno() {
            return phoneno;
        }


        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }


        public String getOtp() {
            return otp;
        }


        public String getPaasport() {
            return paasport;
        }

        public String getDeviceid() {
            return deviceid;
        }


        public String getDate() {
            return date;
        }


        public String getIsactive() {
            return isactive;
        }


    }

    public class LoginUser {
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Data> data = null;

        public String getMessage() {
            return message;
        }
        public Boolean getStatus() {
            return status;
        }
        public List<Data> getData() {
            return data;
        }
        public class Datum {

            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("phoneno")
            @Expose
            private String phoneno;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("paasport")
            @Expose
            private String paasport;
            @SerializedName("deviceid")
            @Expose
            private String deviceid;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("isactive")
            @Expose
            private String isactive;

            public String getUserid() {
                return userid;
            }



            public String getName() {
                return name;
            }



            public String getPhoneno() {
                return phoneno;
            }


            public String getEmail() {
                return email;
            }



            public String getPassword() {
                return password;
            }



            public String getOtp() {
                return otp;
            }


            public String getPaasport() {
                return paasport;
            }



            public String getDeviceid() {
                return deviceid;
            }


            public String getDate() {
                return date;
            }



            public String getIsactive() {
                return isactive;
            }


        }
    }
    public class History {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Data> data = null;

        public String getMessage() {
            return message;
        }


        public Boolean getStatus() {
            return status;
        }


        public List<Data> getData() {
            return data;
        }

        public class Data {

            @SerializedName("historyid")
            @Expose
            private String historyid;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("walletid")
            @Expose
            private String walletid;
            @SerializedName("saleid")
            @Expose
            private String saleid;
            @SerializedName("purchaseid")
            @Expose
            private String purchaseid;
            @SerializedName("purchase_value")
            @Expose
            private String purchaseValue;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("purchasename")
            @Expose
            private String purchasename;
            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("phoneno")
            @Expose
            private String phoneno;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("paasport")
            @Expose
            private String paasport;
            @SerializedName("deviceid")
            @Expose
            private String deviceid;
            @SerializedName("isactive")
            @Expose
            private String isactive;

            @SerializedName("art_gallery_name")
            @Expose
            private String art_gallery_name;

            public String getArtpic() {
                return artpic;
            }

            @SerializedName("artpic")
            @Expose
            private String artpic;
            public String getValue() {
                return value;
            }

            @SerializedName("value")
            @Expose
            private String value;

            public String getArt_gallery_name() {
                return art_gallery_name;
            }

            public String getPurchase_date() {
                return purchase_date;
            }

            @SerializedName("purchase_date")
            @Expose
            private String purchase_date;

            public String getHistoryid() {
                return historyid;
            }


            public String getName() {
                return name;
            }


            public String getWalletid() {
                return walletid;
            }



            public String getSaleid() {
                return saleid;
            }



            public String getPurchaseid() {
                return purchaseid;
            }


            public String getPurchaseValue() {
                return purchaseValue;
            }


            public String getDate() {
                return date;
            }


            public String getPurchasename() {
                return purchasename;
            }



            public String getUserid() {
                return userid;
            }


            public String getPhoneno() {
                return phoneno;
            }


            public String getEmail() {
                return email;
            }


            public String getPassword() {
                return password;
            }


            public String getOtp() {
                return otp;
            }



            public String getPaasport() {
                return paasport;
            }



            public String getDeviceid() {
                return deviceid;
            }


            public String getIsactive() {
                return isactive;
            }


        }
    }
    public class FeatchArt {
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Data> data = null;

        public String getMessage() {
            return message;
        }
       public Boolean getStatus() {
            return status;
        }

        public List<Data> getData() {
            return data;
        }
        public class Data {
            @SerializedName("artid")
            @Expose
            private String artid;
            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("artpic")
            @Expose
            private String artpic;
            @SerializedName("qr_code")
            @Expose
            private String qrCode;
            @SerializedName("painter_name")
            @Expose
            private String painterName;
            @SerializedName("owner_name")
            @Expose
            private String ownerName;
            @SerializedName("art_gallery_name")
            @Expose
            private String artGalleryName;
            @SerializedName("dop")
            @Expose
            private String dop;
            @SerializedName("value")
            @Expose
            private String value;
            @SerializedName("art_type")
            @Expose
            private String artType;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("isactive")
            @Expose
            private String isactive;

            public String getArtid() {
                return artid;
            }
            public String getUserid() {
                return userid;
            }
            public String getArtpic() {
                return artpic;
            }
            public String getQrCode() {
                return qrCode;
            }
            public String getPainterName() {
                return painterName;
            }
            public String getOwnerName() {
                return ownerName;
            }
            public String getArtGalleryName() {
                return artGalleryName;
            }
            public String getDop() {
                return dop;
            }
            public String getValue() {
                return value;
            }
            public String getArtType() {
                return artType;
            }
            public String getDate() {
                return date;
            }
            public String getIsactive() {
                return isactive;
            }
            }
        }

    public class FeatProfile {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Data> data = null;

        public String getMessage() {
            return message;
        }

        public Boolean getStatus() {
            return status;
        }


        public List<Data> getData() {
            return data;
        }

        public class Data {

            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("phoneno")
            @Expose
            private String phoneno;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("paasport")
            @Expose
            private String paasport;
            @SerializedName("deviceid")
            @Expose
            private String deviceid;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("isactive")
            @Expose
            private String isactive;

            public String getUserid() {
                return userid;
            }
            public String getName() {
                return name;
            }
            public String getPhoneno() {
                return phoneno;
            }
            public String getEmail() {
                return email;
            }
            public String getPassword() {
                return password;
            }
            public String getOtp() {
                return otp;
            }
            public String getPaasport() {
                return paasport;
            }
            public String getDeviceid() {
                return deviceid;
            }
            public String getDate() {
                return date;
            }
            public String getIsactive() {
                return isactive;
            }

        }
    }
    public class Status {

        @SerializedName("status")
        @Expose
        private Boolean status;

        public Boolean getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        @SerializedName("message")
        @Expose
        private String message;
    }
}






