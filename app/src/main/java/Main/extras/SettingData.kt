package Main.extras

import Main.Model.response.*
import com.example.sarwan.final_year_project.R
import extras.ApplicationConstants


class SettingData{


    companion object {

        private var iconsData = IconsData()
        private var iconsDataList = ArrayList<IconsData>()

        private var propertiesData = PropertiesData()
        private var propertiesDataList = ArrayList<PropertiesData>()

        private var offersData = OffersData()
        private var offersDataList = ArrayList<OffersData>()

        private var placesData = PlacesData()
        private var placesDataList = ArrayList<PlacesData>()

        private var hotelsData = HotelData()
        private var hotelsDataList = ArrayList<HotelData>()

        fun getIconsData() : ArrayList<IconsData>{

            iconsDataList.clear()

            iconsData = IconsData()

            iconsData.id = 1
            iconsData.title = ApplicationConstants.FLIGHTS
            iconsData.imageUri = "https://preview.ibb.co/kkzJQe/icon_0.png"
            iconsDataList.add(iconsData)
            iconsData = IconsData()

            iconsData.id = 2
            iconsData.title = ApplicationConstants.HOTELS
            iconsData.imageUri = "https://image.ibb.co/j5Y6BK/icon_1.png"
            iconsDataList.add(iconsData)

            iconsData = IconsData()

            iconsData.id = 3
            iconsData.title = ApplicationConstants.HOLIDAYS
            iconsData.imageUri = "https://image.ibb.co/gF2cJz/icon_2.png"
            iconsDataList.add(iconsData)

            iconsData = IconsData()

            iconsData.id = 4
            iconsData.title = ApplicationConstants.BUS
            iconsData.imageUri = "https://preview.ibb.co/iY9eWK/icon_3.png"
            iconsDataList.add(iconsData)

            iconsData = IconsData()

            iconsData.id = 5
            iconsData.title = ApplicationConstants.CARS
            iconsData.imageUri = "https://preview.ibb.co/cxPk5e/icon_4.png"
            iconsDataList.add(iconsData)


            iconsData = IconsData()
            iconsData.id = 6
            iconsData.title = ApplicationConstants.HOMES
            iconsData.imageUri = "https://preview.ibb.co/go6Mdz/icon_5.png"
            iconsDataList.add(iconsData)


            iconsData = IconsData()
            iconsData.id = 7
            iconsData.title = ApplicationConstants.RAILS
            iconsData.imageUri = "https://preview.ibb.co/fkq3rK/icon_6.png"
            iconsDataList.add(iconsData)

            return iconsDataList
        }

        fun getPropertyData(): ArrayList<PropertiesData> {

            propertiesDataList.clear()

            propertiesData = PropertiesData()
            propertiesData.id = 1
            propertiesData.title = ApplicationConstants.ZERO_CANCELLATION
            propertiesData.description = ApplicationConstants.ZERO_CANCELLATION_DESC
            propertiesData.iconArray.add("https://preview.ibb.co/kkzJQe/icon_0.png")
            propertiesData.iconArray.add("https://image.ibb.co/j5Y6BK/icon_1.png")
            propertiesDataList.add(propertiesData)

            propertiesData = PropertiesData()
            propertiesData.id = 2
            propertiesData.title = ApplicationConstants.ADD_ON_SERVICES
            propertiesData.description = ApplicationConstants.ADD_ON_SERVICES_DESC
            propertiesData.iconArray.add("https://preview.ibb.co/iY9eWK/icon_3.png")
            propertiesData.iconArray.add("https://preview.ibb.co/cxPk5e/icon_4.png")
            propertiesData.iconArray.add("https://preview.ibb.co/go6Mdz/icon_5.png")
            propertiesDataList.add(propertiesData)

            propertiesData = PropertiesData()
            propertiesData.id = 3
            propertiesData.title = ApplicationConstants.BEST_PRICE
            propertiesData.description = ApplicationConstants.BEST_PRICE_DESC
            propertiesData.iconArray.add("https://preview.ibb.co/fkq3rK/icon_6.png")
            propertiesDataList.add(propertiesData)

            return propertiesDataList
        }

        fun getOfferData(): ArrayList<OffersData> {
            offersDataList.clear()

            offersData = OffersData()
            offersData.iD = 1
            offersData.title = ApplicationConstants.AVAIL_FREE
            offersData.description = ApplicationConstants.AVAIL_FREE_DESC
            offersData.cardBackground = R.drawable.header_bg_gradient
            offersDataList.add(offersData)

            offersData = OffersData()
            offersData.iD = 2
            offersData.title = ApplicationConstants.GET_MEMBERSHIP
            offersData.description = ApplicationConstants.GET_MEMBERSHIP_DESC
            offersData.cardBackground = R.drawable.header_bg_gradient1
            offersDataList.add(offersData)

            offersData = OffersData()
            offersData.iD = 3
            offersData.title = ApplicationConstants.PAY_WITH_BANK
            offersData.description = ApplicationConstants.PAY_WITH_BANK_DESC
            offersData.cardBackground = R.drawable.header_bg_gradient2
            offersDataList.add(offersData)

            offersData = OffersData()
            offersData.iD = 4
            offersData.title = ApplicationConstants.FREE_FOOD_COUPON
            offersData.description = ApplicationConstants.FREE_FOOD_COUPON_DESC
            offersData.cardBackground = R.drawable.header_bg_gradient3
            offersDataList.add(offersData)

            return offersDataList
        }

        fun getPlacesData(): ArrayList<PlacesData> {
            placesDataList.clear()

            placesData = PlacesData()
            placesData.iD = 1
            placesData.placeName = ApplicationConstants.AVAIL_FREE
            placesData.rating = 4f
            placesData.bgImage = "https://image.ibb.co/evbhke/banner_image_1.png"
            placesDataList.add(placesData)

            placesData = PlacesData()
            placesData.iD = 2
            placesData.placeName = ApplicationConstants.BEST_PRICE
            placesData.rating = 3f
            placesData.bgImage = "https://preview.ibb.co/dMeeWK/banner_image_2.jpg"
            placesDataList.add(placesData)

            placesData = PlacesData()
            placesData.iD = 3
            placesData.placeName = ApplicationConstants.PAY_WITH_BANK
            placesData.rating = 2f
            placesData.bgImage = "https://preview.ibb.co/ep7zWK/banner_image_3.png"
            placesDataList.add(placesData)

            placesData = PlacesData()
            placesData.iD = 4
            placesData.placeName = ApplicationConstants.FREE_FOOD_COUPON_DESC
            placesData.rating = 5f
            placesData.bgImage = "https://image.ibb.co/jS08Qe/banner_image_4.jpg"
            placesDataList.add(placesData)

            return placesDataList
        }

        fun getHotelData(): ArrayList<HotelData> {
            hotelsDataList.clear()

            hotelsData = HotelData()
            hotelsData.iD = 1
            hotelsData.hotelName = ApplicationConstants.HOTEL_NAME
            hotelsData.hotelCityName = "KARACHI"
            hotelsData.rating = 4f
            hotelsData.bannerImage = "https://preview.ibb.co/diyOQe/hotel_banner_1.jpg"
            hotelsData.insideCardImage = arrayListOf("https://preview.ibb.co/cRgtQe/hotel_banner_2.jpg")
            hotelsDataList.add(hotelsData)

            hotelsData = HotelData()
            hotelsData.iD = 2
            hotelsData.hotelName = ApplicationConstants.HOTEL_NAME
            hotelsData.hotelCityName = "KARACHI"
            hotelsData.rating = 1f
            hotelsData.bannerImage = "https://preview.ibb.co/cRgtQe/hotel_banner_2.jpg"
            hotelsData.insideCardImage = arrayListOf("https://preview.ibb.co/bOBtQe/hotel_banner_3.jpg")
            hotelsDataList.add(hotelsData)

            hotelsData = HotelData()
            hotelsData.iD = 3
            hotelsData.hotelName = ApplicationConstants.HOTEL_NAME
            hotelsData.hotelCityName = "KARACHI"
            hotelsData.rating = 2f
            hotelsData.bannerImage = "https://preview.ibb.co/bOBtQe/hotel_banner_3.jpg"
            hotelsData.insideCardImage = arrayListOf("https://preview.ibb.co/j7qL5e/hotel_banner_4.jpg")
            hotelsDataList.add(hotelsData)

            hotelsData = HotelData()
            hotelsData.iD = 4
            hotelsData.hotelName = ApplicationConstants.HOTEL_NAME
            hotelsData.hotelCityName = "KARACHI"
            hotelsData.rating = 2f
            hotelsData.bannerImage = "https://preview.ibb.co/j7qL5e/hotel_banner_4.jpg"
            hotelsData.insideCardImage = arrayListOf("https://preview.ibb.co/msTJrK/hotel_banner_5.jpg")
            hotelsDataList.add(hotelsData)

            hotelsData = HotelData()
            hotelsData.iD = 5
            hotelsData.hotelName = ApplicationConstants.HOTEL_NAME
            hotelsData.hotelCityName = "KARACHI"
            hotelsData.rating = 5f
            hotelsData.bannerImage = "https://preview.ibb.co/msTJrK/hotel_banner_5.jpg"
            hotelsData.insideCardImage = arrayListOf("https://preview.ibb.co/diyOQe/hotel_banner_1.jpg")
            hotelsDataList.add(hotelsData)

            return hotelsDataList
        }
    }
}