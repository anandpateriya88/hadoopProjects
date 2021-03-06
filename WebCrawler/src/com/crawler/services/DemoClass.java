package com.crawler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.arch.domainobjects.City;
import com.arch.domainobjects.Country;
import com.arch.domainobjects.PropertyDetails;
import com.arch.domainobjects.State;
import com.google.gson.Gson;

public class DemoClass {

	private static Country countryStateCity;

	
	public void setCountry(){
		System.out.println("CrimeDataMapper : setCountry STARTED");
		String countryObj = "{'states':[{'id':'KL','name':'Kuala Lumpur','cities':[{'city':'Ampang'},{'city':'Ampang Hilir'},{'city':'Bandar Damai Perdana'},{'city':'Bandar Menjalara'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Tasik Selatan'},{'city':'Bangsar'},{'city':'Bangsar South'},{'city':'Batu'},{'city':'Brickfields'},{'city':'Bukit Bintang'},{'city':'Bukit Jalil'},{'city':'Bukit Ledang'},{'city':'Bukit Tunku'},{'city':'Chan Sow Lin'},{'city':'Cheras'},{'city':'city Centre'},{'city':'Country Heights Damansara'},{'city':'Damansara'},{'city':'Damansara Heights'},{'city':'Desa Pandan'},{'city':'Desa Park city'},{'city':'Desa Petaling'},{'city':'Federal Hill'},{'city':'Gombak'},{'city':'Jalan Ipoh'},{'city':'Jalan Klang Lama'},{'city':'Jalan Kuching'},{'city':'Jalan Sultan Ismail'},{'city':'Jinjang'},{'city':'Kenny Hills'},{'city':'Kepong'},{'city':'Keramat'},{'city':'KL city'},{'city':'KL Sentral'},{'city':'KLCC'},{'city':'Kuchai Lama'},{'city':'Mid Valley city'},{'city':'Mont Kiara'},{'city':'Old Klang Road'},{'city':'OUG'},{'city':'Pandan Indah'},{'city':'Pandan Jaya'},{'city':'Pandan Perdana'},{'city':'Pantai'},{'city':'Pekan Batu'},{'city':'Puchong'},{'city':'Salak Selatan'},{'city':'Segambut'},{'city':'Sentul'},{'city':'Seputeh'},{'city':'Serdang'},{'city':'Setapak'},{'city':'Setiawangsa'},{'city':'Solaris Dutamas'},{'city':'Solaris Mont Kiara'},{'city':'Sri Hartamas'},{'city':'Sri Petaling'},{'city':'Sunway SPK'},{'city':'Sungai Besi'},{'city':'Sungai Penchala'},{'city':'Taman Desa'},{'city':'Taman Duta'},{'city':'Taman Melawati'},{'city':'Taman Tun Dr Ismail'},{'city':'Titiwangsa'},{'city':'Wangsa Maju'}]},{'id':'JO','name':'Johor','cities':[{'city':'Ayer Baloi'},{'city':'Ayer Hitam'},{'city':'Bakri'},{'city':'Batu Anam'},{'city':'Batu Pahat'},{'city':'Bekok'},{'city':'Benut'},{'city':'Bukit Gambir'},{'city':'Bukit Pasir'},{'city':'Chaah'},{'city':'Endau'},{'city':'Gelang Patah'},{'city':'Gerisek'},{'city':'Gugusan Taib Andak'},{'city':'Horizon Hills'},{'city':'Jementah'},{'city':'Johor Bahru'},{'city':'Kahang'},{'city':'Kampung Kenangan Tun Dr Ismail'},{'city':'Kluang'},{'city':'Kota Tinggi'},{'city':'Kukup'},{'city':'Kulai'},{'city':'Labis'},{'city':'Layang Layang'},{'city':'Masai'},{'city':'Medini'},{'city':'Mersing'},{'city':'Muar'},{'city':'Nusajaya'},{'city':'Pagoh'},{'city':'Paloh'},{'city':'Panchor'},{'city':'Parit Jawa'},{'city':'Parit Raja'},{'city':'Parit Sulong'},{'city':'Pasir Gudang'},{'city':'Pekan Nanas'},{'city':'Pengerang'},{'city':'Permas Jaya'},{'city':'Plentong'},{'city':'Pontian'},{'city':'Puteri Harbour'},{'city':'Rengam'},{'city':'Rengit'},{'city':'Segamat'},{'city':'Semerah'},{'city':'Senai'},{'city':'Senggarang'},{'city':'Senibong'},{'city':'Seri Gadang'},{'city':'Setia Indah'},{'city':'Setia Tropika'},{'city':'Simpang Rengam'},{'city':'Skudai'},{'city':'Sungai Mati'},{'city':'Tampoi'},{'city':'Tangkak'},{'city':'Ulu Tiram'},{'city':'Yong Peng'}]},{'id':'KE','name':'Kedah','cities':[{'city':'Alor Setar'},{'city':'Ayer Hitam'},{'city':'Baling'},{'city':'Bandar Baharu'},{'city':'Bedong'},{'city':'Bukit Kayu Hitam'},{'city':'Guar Chempedak'},{'city':'Gurun'},{'city':'Jitra'},{'city':'Karangan'},{'city':'Kepala Batas'},{'city':'Kodiang'},{'city':'Kota Sarang Semut'},{'city':'Kuala Kedah'},{'city':'Kuala Ketil'},{'city':'Kuala Muda'},{'city':'Kuala Nerang'},{'city':'Kubang Pasu'},{'city':'Kulim'},{'city':'Kupang'},{'city':'Langgar'},{'city':'Langkawi'},{'city':'Lunas'},{'city':'Merbok'},{'city':'Padang Serai'},{'city':'Padang Terap'},{'city':'Pendang'},{'city':'Pokok Sena'},{'city':'Pulau Langkawi'},{'city':'Serdang'},{'city':'Sik'},{'city':'Simpang Empat'},{'city':'Sungai Petani'},{'city':'Univesity Utara'},{'city':'Yan'}]},{'id':'KT','name':'Kelantan','cities':[{'city':'Ayer Lanas'},{'city':'Bachok'},{'city':'Cherang Ruku'},{'city':'Dabong'},{'city':'Gua Musang'},{'city':'Jeli'},{'city':'Kem Desa Pahwalan'},{'city':'Ketereh'},{'city':'Kota Bharu'},{'city':'Kuala Balah'},{'city':'Kuala Kerai'},{'city':'Machang'},{'city':'Melor'},{'city':'Pasir Mas'},{'city':'Pasir Puteh'},{'city':'Pulai Chondong'},{'city':'Rantau Panjang'},{'city':'Selising'},{'city':'Tanah Merah'},{'city':'Tawang'},{'city':'Temangan'},{'city':'Tumpat'},{'city':'Wakaf Baru'}]},{'id':'MA','name':'Melaka','cities':[{'city':'Alor Gajah'},{'city':'Asahan'},{'city':'Ayer Keroh'},{'city':'Bandar Hilir'},{'city':'Batu Berendam'},{'city':'Bemban'},{'city':'Bukit Beruang'},{'city':'Durian Tunggal'},{'city':'Jasin'},{'city':'Kuala Linggi'},{'city':'Kuala Sungai Baru'},{'city':'Lubok China'},{'city':'Masjid Tanah'},{'city':'Melaka Tengah'},{'city':'Merlimau'},{'city':'Selandar'},{'city':'Sungai Rambai'},{'city':'Sungai Udang'},{'city':'Tanjong Kling'},{'city':'Ujong Pasir'}]},{'id':'NS','name':'Negeri Sembilan','cities':[{'city':'Bahau'},{'city':'Bandar Baru Serting'},{'city':'Batang Melaka'},{'city':'Batu Kikir'},{'city':'Gemas'},{'city':'Gemencheh'},{'city':'Jelebu'},{'city':'Jempol'},{'city':'Johol'},{'city':'Juasseh'},{'city':'Kota'},{'city':'Kuala Klawang'},{'city':'Kuala Pilah'},{'city':'Labu'},{'city':'Lenggeng'},{'city':'Linggi'},{'city':'Mantin'},{'city':'Nilai'},{'city':'Pasir Panjang'},{'city':'Pedas'},{'city':'Port Dickson'},{'city':'Rantau'},{'city':'Rembau'},{'city':'Rompin'},{'city':'Senawang'},{'city':'Seremban'},{'city':'Siliau'},{'city':'Simpang Durian'},{'city':'Simpang Pertang'},{'city':'Sri Menanti'},{'city':'Sri Rusa'},{'city':'Tampin'},{'city':'Tanjong Ipoh'}]},{'id':'OT','name':'Other','cities':[]},{'id':'PA','name':'Pahang','cities':[{'city':'Balok'},{'city':'Bandar Pusat Jengka'},{'city':'Bandar Tun Abdul Razak'},{'city':'Benta'},{'city':'Bentong'},{'city':'Bera'},{'city':'Brinchang'},{'city':'Bukit Fraser'},{'city':'Cameron Highlands'},{'city':'Chenor'},{'city':'Daerah Rompin'},{'city':'Damak'},{'city':'Dong'},{'city':'Genting Highlands'},{'city':'Jerantut'},{'city':'Karak'},{'city':'Kuala Lipis'},{'city':'Kuala Rompin'},{'city':'Kuantan'},{'city':'Lanchang'},{'city':'Lurah Bilut'},{'city':'Maran'},{'city':'Mengkarak'},{'city':'Mentakab'},{'city':'Muadzam Shah'},{'city':'Padang Tengku'},{'city':'Pekan'},{'city':'Raub'},{'city':'Ringlet'},{'city':'Rompin'},{'city':'Sega'},{'city':'Sungai Koyan'},{'city':'Sungai Lembing'},{'city':'Sungai Ruan'},{'city':'Tanah Rata'},{'city':'Temerloh'},{'city':'Triang'}]},{'id':'PE','name':'Penang','cities':[{'city':'Air Tawar'},{'city':'Alma'},{'city':'Ayer Itam'},{'city':'Bagan Ajam'},{'city':'Bagan Jermal'},{'city':'Bagan Lallang'},{'city':'Balik Pulau'},{'city':'Bandar Perda'},{'city':'Batu Ferringhi'},{'city':'Batu Kawan'},{'city':'Batu Maung'},{'city':'Batu Uban'},{'city':'Bayan Baru'},{'city':'Bayan Lepas'},{'city':'Berapit'},{'city':'Bertam'},{'city':'Bukit Dumbar'},{'city':'Bukit Jambul'},{'city':'Bukit Mertajam'},{'city':'Bukit Minyak'},{'city':'Bukit Tambun'},{'city':'Bukit Tengah'},{'city':'Butterworth'},{'city':'Gelugor'},{'city':'Georgetown'},{'city':'Gertak Sangul'},{'city':'Greenlane'},{'city':'Jawi'},{'city':'Jelutong'},{'city':'Juru'},{'city':'Kepala Batas'},{'city':'Kubang Semang'},{'city':'Mak Mandin'},{'city':'Minden Heights'},{'city':'Nibong Tebal'},{'city':'Pauh Jaya'},{'city':'Paya Terubong'},{'city':'Penaga'},{'city':'Penang Hill'},{'city':'Penanti'},{'city':'Perai'},{'city':'Permatang Kuching'},{'city':'Permatang Pauh'},{'city':'Permatang Tinggi'},{'city':'Persiaran Gurney'},{'city':'Prai'},{'city':'Pulau Betong'},{'city':'Pulau Tikus'},{'city':'Raja Uda'},{'city':'Relau'},{'city':'Scotland'},{'city':'Seberang Jaya'},{'city':'Seberang Perai'},{'city':'Serdang'},{'city':'Simpang Ampat'},{'city':'Sungai Ara'},{'city':'Sungai Bakap'},{'city':'Sungai Dua'},{'city':'Sungai Jawi'},{'city':'Sungai Nibong'},{'city':'Sungai Pinang'},{'city':'Tanjong Tokong'},{'city':'Tanjung Bungah'},{'city':'Tasek Gelugor'},{'city':'Teluk Bahang'},{'city':'Teluk Kumbar'},{'city':'USM'},{'city':'Valdor'}]},{'id':'PK','name':'Perak','cities':[{'city':'Ayer Tawar'},{'city':'Bagan Datoh'},{'city':'Bagan Serai'},{'city':'Batu Gajah'},{'city':'Batu Kurau'},{'city':'Behrang Stesen'},{'city':'Beruas'},{'city':'Bidor'},{'city':'Bota'},{'city':'Changkat Jering'},{'city':'Changkat Keruing'},{'city':'Chemor'},{'city':'Chenderiang'},{'city':'Chenderong Balai'},{'city':'Chikus'},{'city':'Enggor'},{'city':'Gerik'},{'city':'Gopeng'},{'city':'Hutan Melintang'},{'city':'Intan'},{'city':'Ipoh'},{'city':'Jeram'},{'city':'Kampar'},{'city':'Kampong Gajah'},{'city':'Kampong Kepayang'},{'city':'Kamunting'},{'city':'Kuala Kangsar'},{'city':'Kuala Kurau'},{'city':'Kuala Sepetang'},{'city':'Lahat'},{'city':'Lambor Kanan'},{'city':'Langkap'},{'city':'Lenggong'},{'city':'Lumut'},{'city':'Malim Nawar'},{'city':'Mambang Diawan'},{'city':'Manong'},{'city':'Matang'},{'city':'Menglembu'},{'city':'Padang Rengas'},{'city':'Pangkor'},{'city':'Pantai Remis'},{'city':'Parit'},{'city':'Parit Buntar'},{'city':'Pengkalan Hulu'},{'city':'Pusing'},{'city':'Rantau Panjang'},{'city':'Sauk'},{'city':'Selama'},{'city':'Selekoh'},{'city':'Selinsing'},{'city':'Semanggol'},{'city':'Seri Manjong'},{'city':'Simpang'},{'city':'Sitiawan'},{'city':'Slim River'},{'city':'Sungai Siput'},{'city':'Sungai Sumun'},{'city':'Sungkai'},{'city':'Taiping'},{'city':'Tanjong Piandang'},{'city':'Tanjong Rambutan'},{'city':'Tanjong Tualang'},{'city':'Tanjung Malim'},{'city':'Tapah'},{'city':'Teluk Intan'},{'city':'Temoh'},{'city':'TLDM Lumut'},{'city':'Trolak'},{'city':'Trong'},{'city':'Tronoh'},{'city':'Ulu Bernam'},{'city':'Ulu Kinta'}]},{'id':'PL','name':'Perlis','cities':[{'city':'Arau'},{'city':'Kaki Bukit'},{'city':'Kangar'},{'city':'Kuala Perlis'},{'city':'Padang Besar'},{'city':'Pauh'},{'city':'Simpang Ampat'}]},{'id':'PJ','name':'Putrajaya','cities':[{'city':'Cyberjaya'},{'city':'Putrajaya'}]},{'id':'SA','name':'Sabah','cities':[{'city':'Beaufort'},{'city':'Beluran'},{'city':'Bongawan'},{'city':'Keningau'},{'city':'Kota Belud'},{'city':'Kota Kinabalu'},{'city':'Kota Kinabatangan'},{'city':'Kota Marudu'},{'city':'Kuala Penyu'},{'city':'Kudat'},{'city':'Kunak'},{'city':'Lahad Datu'},{'city':'Likas'},{'city':'Membakut'},{'city':'Menumbok'},{'city':'Nabawan'},{'city':'Pamol'},{'city':'Papar'},{'city':'Penampang'},{'city':'Pitas'},{'city':'Pulatan'},{'city':'Ranau'},{'city':'Sandakan'},{'city':'Semporna'},{'city':'Sipitang'},{'city':'Tambunan'},{'city':'Tamparuli'},{'city':'Tawau'},{'city':'Tenom'},{'city':'Tuaran'}]},{'id':'SW','name':'Sarawak','cities':[{'city':'Asajaya'},{'city':'Balingian'},{'city':'Baram'},{'city':'Bau'},{'city':'Bekenu'},{'city':'Belaga'},{'city':'Belawai'},{'city':'Betong'},{'city':'Bintagor'},{'city':'Bintulu'},{'city':'Dalat'},{'city':'Daro'},{'city':'Debak'},{'city':'Engkilili'},{'city':'Julau'},{'city':'Kabong'},{'city':'Kanowit'},{'city':'Kapit'},{'city':'Kota Samarahan'},{'city':'Kuching'},{'city':'Lawas'},{'city':'Limbang'},{'city':'Lingga'},{'city':'Long Lama'},{'city':'Lubok Antu'},{'city':'Lundu'},{'city':'Lutong'},{'city':'Maradong'},{'city':'Marudi'},{'city':'Matu'},{'city':'Miri'},{'city':'Mukah'},{'city':'Nanga Medamit'},{'city':'Niah'},{'city':'Pusa'},{'city':'Roban'},{'city':'Saratok'},{'city':'Sarikei'},{'city':'Sebauh'},{'city':'Sebuyau'},{'city':'Serian'},{'city':'Sibu'},{'city':'Simunjan'},{'city':'Song'},{'city':'Spaoh'},{'city':'Sri Aman'},{'city':'Sundar'},{'city':'Tanjung Kidurong'},{'city':'Tatau'}]},{'id':'SE','name':'Selangor','cities':[{'city':'Alam Impian'},{'city':'Aman Perdana'},{'city':'Ambang Botanic'},{'city':'Ampang'},{'city':'Ara Damansara'},{'city':'Balakong'},{'city':'Bandar Botanic'},{'city':'Bandar Bukit Raja'},{'city':'Bandar Bukit Tinggi'},{'city':'Bandar Kinrara'},{'city':'Bandar Puncak Alam'},{'city':'Bandar Puteri Klang'},{'city':'Bandar Puteri Puchong'},{'city':'Bandar Saujana Putra'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Sungai Long'},{'city':'Bandar Sunway'},{'city':'Bandar Utama'},{'city':'Bangi'},{'city':'Banting'},{'city':'Batang Berjuntai'},{'city':'Batang Kali'},{'city':'Batu Arang'},{'city':'Batu Caves'},{'city':'Beranang'},{'city':'Bukit Jelutong'},{'city':'Bukit Rahman Putra'},{'city':'Bukit Rotan'},{'city':'Bukit Subang'},{'city':'Cheras'},{'city':'Country Heights'},{'city':'Cyberjaya'},{'city':'Damansara Damai'},{'city':'Damansara Intan'},{'city':'Damansara Jaya'},{'city':'Damansara Kim'},{'city':'Damansara Perdana'},{'city':'Damansara Utama'},{'city':'Denai Alam'},{'city':'Dengkil'},{'city':'Glenmarie'},{'city':'Gombak'},{'city':'Hulu Langat'},{'city':'Hulu Selangor'},{'city':'Jade Hills'},{'city':'Jenjarom'},{'city':'Kajang'},{'city':'Kapar'},{'city':'Kayu Ara'},{'city':'Kelana Jaya'},{'city':'Kerling'},{'city':'Klang'},{'city':'Kota Damansara'},{'city':'Kota Emerald'},{'city':'Kota Kemuning'},{'city':'Kuala Kubu Baru'},{'city':'Kuala Langat'},{'city':'Kuala Selangor'},{'city':'Kuang'},{'city':'Mutiara Damansara'},{'city':'Nilai'},{'city':'Petaling Jaya'},{'city':'Port Klang'},{'city':'Puchong'},{'city':'Puchong South'},{'city':'Pulau  Indah ( Pulau Lumut)'},{'city':'Pulau Carey '},{'city':'Pulau Ketam'},{'city':'Puncak Jalil'},{'city':'Putra Heights'},{'city':'Rasa'},{'city':'Rawang'},{'city':'Sabak Bernam'},{'city':'Saujana'},{'city':'Sekinchan'},{'city':'Selayang'},{'city':'Semenyih'},{'city':'Sepang'},{'city':'Serdang'},{'city':'Serendah'},{'city':'Seri Kembangan'},{'city':'Setia Alam'},{'city':'Setia Eco Park'},{'city':'Shah Alam'},{'city':'SierraMas'},{'city':'SS2'},{'city':'Subang Bestari'},{'city':'Subang Heights'},{'city':'Subang Jaya'},{'city':'Sungai Ayer Tawar'},{'city':'Sungai Besar'},{'city':'Sungai Buloh'},{'city':'Sungai Pelek'},{'city':'Taman Melawati'},{'city':'Taman TTDI Jaya'},{'city':'Tanjong Karang'},{'city':'Tanjong Sepat'},{'city':'Telok Panglima Garang'},{'city':'Tropicana'},{'city':'Ulu Klang'},{'city':'USJ'},{'city':'USJ Heights'},{'city':'Valencia'}]},{'id':'TR','name':'Terengganu','cities':[{'city':'Besut'},{'city':'Dungun'},{'city':'Hulu Terengganu'},{'city':'Kemaman'},{'city':'Kuala Terengganu'},{'city':'Marang'},{'city':'Setiu'},{'city':'Kerteh'}]}]}";
		
		Gson gson = new Gson();
		Country obj = gson.fromJson(countryObj, Country.class);
		DemoClass.countryStateCity = obj;
		System.out.println("CrimeDataMapper : setCountry Coutnry List STATE Size=>"+this.countryStateCity.getStates().size());
		System.out.println("CrimeDataMapper : setCountry END");
	}
	
	
	public static void main(String str[]) {
//		List<PropertyDetails> propDetlsList = new ArrayList<PropertyDetails>();
		SpiderLeg sl = new SpiderLeg();
		Spider spider = new Spider();
		DemoClass dc = new DemoClass();
		dc.getCountry();
		dc.setCountry();
	
			
		
	//	downloadPhotos("http://pictures.my.ippstatic.com/realtors/images/120/21144/06d1a3fc964141d4a6e1964b1f8b9c26.jpg", "image1");
			/*try {
			File file = new File("G:\\crawlingAll\\siteUrlsAll.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    int noOfOutputFiles = 1;
		    while ((line = br.readLine()) != null) {
		    	String urlAndDepthArray[] = line.split("::");
				if(null != urlAndDepthArray && urlAndDepthArray.length>0){
				int depth = Integer.parseInt(urlAndDepthArray[1]);
				List<PropertyDetails> allPropoertiesLst = spider.getAllPropertiesDtls(urlAndDepthArray[0], depth);
				//List<PropertyDetails> allPropoertiesNameOnly = new ArrayList<PropertyDetails>();

				if(!allPropoertiesLst.isEmpty()){
					for(PropertyDetails propTemp : allPropoertiesLst){
						PropertyDetails tempOnlyName = new PropertyDetails();
						tempOnlyName.setName(propTemp.getName());
						allPropoertiesNameOnly.add(tempOnlyName);
					}
					if(urlAndDepthArray[0].contains("mitula")){
						String floorArea = allPropoertiesLst.get(0).getFloorArea();
						System.out.println("Floor Area ===> "+floorArea.split(" ")[0]);
					}
					Gson gson = new Gson();
					String propDetailsJson = gson.toJson(allPropoertiesLst);
					
					try {
						PrintWriter writer = new PrintWriter("G:\\crawlingAll\\localOutput\\property"+noOfOutputFiles+".txt", "UTF-8");
						writer.println(propDetailsJson);
						writer.close();
						
					} catch (Exception e) {
						System.out.println("Exception occured in Writing file to FS -- " + e);
					}finally{
						noOfOutputFiles++;
					}
				}
				}
		    }
		}catch(Exception e){
			System.out.println("Exception Occured at start up .. "+e);
		}finally{
	
		}*/
		
		//-- to test individual urls for data --
		// http://www.propertyguru.com.my/property-for-sale/2
		
	/*	String url = "http://thinkproperty.com.my/property-listings/buy/search/Page-1-100.html?cb_ownerlist=0&max_price=0&max_rent=0&min_price=0&min_rent=0&prop_type=0&search=Search&sort_by=DESCUPDATE";
		String success = sl.testsiteForData(url);
		System.out.println("========>"+success);*/
	}
	
	
	public Country getCountry(){
		
		String countryObj = "{'states':[{'id':'KL','name':'Kuala Lumpur','cities':[{'city':'Ampang'},{'city':'Ampang Hilir'},{'city':'Bandar Damai Perdana'},{'city':'Bandar Menjalara'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Tasik Selatan'},{'city':'Bangsar'},{'city':'Bangsar South'},{'city':'Batu'},{'city':'Brickfields'},{'city':'Bukit Bintang'},{'city':'Bukit Jalil'},{'city':'Bukit Ledang'},{'city':'Bukit Tunku'},{'city':'Chan Sow Lin'},{'city':'Cheras'},{'city':'city Centre'},{'city':'Country Heights Damansara'},{'city':'Damansara'},{'city':'Damansara Heights'},{'city':'Desa Pandan'},{'city':'Desa Park city'},{'city':'Desa Petaling'},{'city':'Federal Hill'},{'city':'Gombak'},{'city':'Jalan Ipoh'},{'city':'Jalan Klang Lama'},{'city':'Jalan Kuching'},{'city':'Jalan Sultan Ismail'},{'city':'Jinjang'},{'city':'Kenny Hills'},{'city':'Kepong'},{'city':'Keramat'},{'city':'KL city'},{'city':'KL Sentral'},{'city':'KLCC'},{'city':'Kuchai Lama'},{'city':'Mid Valley city'},{'city':'Mont Kiara'},{'city':'Old Klang Road'},{'city':'OUG'},{'city':'Pandan Indah'},{'city':'Pandan Jaya'},{'city':'Pandan Perdana'},{'city':'Pantai'},{'city':'Pekan Batu'},{'city':'Puchong'},{'city':'Salak Selatan'},{'city':'Segambut'},{'city':'Sentul'},{'city':'Seputeh'},{'city':'Serdang'},{'city':'Setapak'},{'city':'Setiawangsa'},{'city':'Solaris Dutamas'},{'city':'Solaris Mont Kiara'},{'city':'Sri Hartamas'},{'city':'Sri Petaling'},{'city':'Sunway SPK'},{'city':'Sungai Besi'},{'city':'Sungai Penchala'},{'city':'Taman Desa'},{'city':'Taman Duta'},{'city':'Taman Melawati'},{'city':'Taman Tun Dr Ismail'},{'city':'Titiwangsa'},{'city':'Wangsa Maju'}]},{'id':'JO','name':'Johor','cities':[{'city':'Ayer Baloi'},{'city':'Ayer Hitam'},{'city':'Bakri'},{'city':'Batu Anam'},{'city':'Batu Pahat'},{'city':'Bekok'},{'city':'Benut'},{'city':'Bukit Gambir'},{'city':'Bukit Pasir'},{'city':'Chaah'},{'city':'Endau'},{'city':'Gelang Patah'},{'city':'Gerisek'},{'city':'Gugusan Taib Andak'},{'city':'Horizon Hills'},{'city':'Jementah'},{'city':'Johor Bahru'},{'city':'Kahang'},{'city':'Kampung Kenangan Tun Dr Ismail'},{'city':'Kluang'},{'city':'Kota Tinggi'},{'city':'Kukup'},{'city':'Kulai'},{'city':'Labis'},{'city':'Layang Layang'},{'city':'Masai'},{'city':'Medini'},{'city':'Mersing'},{'city':'Muar'},{'city':'Nusajaya'},{'city':'Pagoh'},{'city':'Paloh'},{'city':'Panchor'},{'city':'Parit Jawa'},{'city':'Parit Raja'},{'city':'Parit Sulong'},{'city':'Pasir Gudang'},{'city':'Pekan Nanas'},{'city':'Pengerang'},{'city':'Permas Jaya'},{'city':'Plentong'},{'city':'Pontian'},{'city':'Puteri Harbour'},{'city':'Rengam'},{'city':'Rengit'},{'city':'Segamat'},{'city':'Semerah'},{'city':'Senai'},{'city':'Senggarang'},{'city':'Senibong'},{'city':'Seri Gadang'},{'city':'Setia Indah'},{'city':'Setia Tropika'},{'city':'Simpang Rengam'},{'city':'Skudai'},{'city':'Sungai Mati'},{'city':'Tampoi'},{'city':'Tangkak'},{'city':'Ulu Tiram'},{'city':'Yong Peng'}]},{'id':'KE','name':'Kedah','cities':[{'city':'Alor Setar'},{'city':'Ayer Hitam'},{'city':'Baling'},{'city':'Bandar Baharu'},{'city':'Bedong'},{'city':'Bukit Kayu Hitam'},{'city':'Guar Chempedak'},{'city':'Gurun'},{'city':'Jitra'},{'city':'Karangan'},{'city':'Kepala Batas'},{'city':'Kodiang'},{'city':'Kota Sarang Semut'},{'city':'Kuala Kedah'},{'city':'Kuala Ketil'},{'city':'Kuala Muda'},{'city':'Kuala Nerang'},{'city':'Kubang Pasu'},{'city':'Kulim'},{'city':'Kupang'},{'city':'Langgar'},{'city':'Langkawi'},{'city':'Lunas'},{'city':'Merbok'},{'city':'Padang Serai'},{'city':'Padang Terap'},{'city':'Pendang'},{'city':'Pokok Sena'},{'city':'Pulau Langkawi'},{'city':'Serdang'},{'city':'Sik'},{'city':'Simpang Empat'},{'city':'Sungai Petani'},{'city':'Univesity Utara'},{'city':'Yan'}]},{'id':'KT','name':'Kelantan','cities':[{'city':'Ayer Lanas'},{'city':'Bachok'},{'city':'Cherang Ruku'},{'city':'Dabong'},{'city':'Gua Musang'},{'city':'Jeli'},{'city':'Kem Desa Pahwalan'},{'city':'Ketereh'},{'city':'Kota Bharu'},{'city':'Kuala Balah'},{'city':'Kuala Kerai'},{'city':'Machang'},{'city':'Melor'},{'city':'Pasir Mas'},{'city':'Pasir Puteh'},{'city':'Pulai Chondong'},{'city':'Rantau Panjang'},{'city':'Selising'},{'city':'Tanah Merah'},{'city':'Tawang'},{'city':'Temangan'},{'city':'Tumpat'},{'city':'Wakaf Baru'}]},{'id':'MA','name':'Melaka','cities':[{'city':'Alor Gajah'},{'city':'Asahan'},{'city':'Ayer Keroh'},{'city':'Bandar Hilir'},{'city':'Batu Berendam'},{'city':'Bemban'},{'city':'Bukit Beruang'},{'city':'Durian Tunggal'},{'city':'Jasin'},{'city':'Kuala Linggi'},{'city':'Kuala Sungai Baru'},{'city':'Lubok China'},{'city':'Masjid Tanah'},{'city':'Melaka Tengah'},{'city':'Merlimau'},{'city':'Selandar'},{'city':'Sungai Rambai'},{'city':'Sungai Udang'},{'city':'Tanjong Kling'},{'city':'Ujong Pasir'}]},{'id':'NS','name':'Negeri Sembilan','cities':[{'city':'Bahau'},{'city':'Bandar Baru Serting'},{'city':'Batang Melaka'},{'city':'Batu Kikir'},{'city':'Gemas'},{'city':'Gemencheh'},{'city':'Jelebu'},{'city':'Jempol'},{'city':'Johol'},{'city':'Juasseh'},{'city':'Kota'},{'city':'Kuala Klawang'},{'city':'Kuala Pilah'},{'city':'Labu'},{'city':'Lenggeng'},{'city':'Linggi'},{'city':'Mantin'},{'city':'Nilai'},{'city':'Pasir Panjang'},{'city':'Pedas'},{'city':'Port Dickson'},{'city':'Rantau'},{'city':'Rembau'},{'city':'Rompin'},{'city':'Senawang'},{'city':'Seremban'},{'city':'Siliau'},{'city':'Simpang Durian'},{'city':'Simpang Pertang'},{'city':'Sri Menanti'},{'city':'Sri Rusa'},{'city':'Tampin'},{'city':'Tanjong Ipoh'}]},{'id':'OT','name':'Other','cities':[]},{'id':'PA','name':'Pahang','cities':[{'city':'Balok'},{'city':'Bandar Pusat Jengka'},{'city':'Bandar Tun Abdul Razak'},{'city':'Benta'},{'city':'Bentong'},{'city':'Bera'},{'city':'Brinchang'},{'city':'Bukit Fraser'},{'city':'Cameron Highlands'},{'city':'Chenor'},{'city':'Daerah Rompin'},{'city':'Damak'},{'city':'Dong'},{'city':'Genting Highlands'},{'city':'Jerantut'},{'city':'Karak'},{'city':'Kuala Lipis'},{'city':'Kuala Rompin'},{'city':'Kuantan'},{'city':'Lanchang'},{'city':'Lurah Bilut'},{'city':'Maran'},{'city':'Mengkarak'},{'city':'Mentakab'},{'city':'Muadzam Shah'},{'city':'Padang Tengku'},{'city':'Pekan'},{'city':'Raub'},{'city':'Ringlet'},{'city':'Rompin'},{'city':'Sega'},{'city':'Sungai Koyan'},{'city':'Sungai Lembing'},{'city':'Sungai Ruan'},{'city':'Tanah Rata'},{'city':'Temerloh'},{'city':'Triang'}]},{'id':'PE','name':'Penang','cities':[{'city':'Air Tawar'},{'city':'Alma'},{'city':'Ayer Itam'},{'city':'Bagan Ajam'},{'city':'Bagan Jermal'},{'city':'Bagan Lallang'},{'city':'Balik Pulau'},{'city':'Bandar Perda'},{'city':'Batu Ferringhi'},{'city':'Batu Kawan'},{'city':'Batu Maung'},{'city':'Batu Uban'},{'city':'Bayan Baru'},{'city':'Bayan Lepas'},{'city':'Berapit'},{'city':'Bertam'},{'city':'Bukit Dumbar'},{'city':'Bukit Jambul'},{'city':'Bukit Mertajam'},{'city':'Bukit Minyak'},{'city':'Bukit Tambun'},{'city':'Bukit Tengah'},{'city':'Butterworth'},{'city':'Gelugor'},{'city':'Georgetown'},{'city':'Gertak Sangul'},{'city':'Greenlane'},{'city':'Jawi'},{'city':'Jelutong'},{'city':'Juru'},{'city':'Kepala Batas'},{'city':'Kubang Semang'},{'city':'Mak Mandin'},{'city':'Minden Heights'},{'city':'Nibong Tebal'},{'city':'Pauh Jaya'},{'city':'Paya Terubong'},{'city':'Penaga'},{'city':'Penang Hill'},{'city':'Penanti'},{'city':'Perai'},{'city':'Permatang Kuching'},{'city':'Permatang Pauh'},{'city':'Permatang Tinggi'},{'city':'Persiaran Gurney'},{'city':'Prai'},{'city':'Pulau Betong'},{'city':'Pulau Tikus'},{'city':'Raja Uda'},{'city':'Relau'},{'city':'Scotland'},{'city':'Seberang Jaya'},{'city':'Seberang Perai'},{'city':'Serdang'},{'city':'Simpang Ampat'},{'city':'Sungai Ara'},{'city':'Sungai Bakap'},{'city':'Sungai Dua'},{'city':'Sungai Jawi'},{'city':'Sungai Nibong'},{'city':'Sungai Pinang'},{'city':'Tanjong Tokong'},{'city':'Tanjung Bungah'},{'city':'Tasek Gelugor'},{'city':'Teluk Bahang'},{'city':'Teluk Kumbar'},{'city':'USM'},{'city':'Valdor'}]},{'id':'PK','name':'Perak','cities':[{'city':'Ayer Tawar'},{'city':'Bagan Datoh'},{'city':'Bagan Serai'},{'city':'Batu Gajah'},{'city':'Batu Kurau'},{'city':'Behrang Stesen'},{'city':'Beruas'},{'city':'Bidor'},{'city':'Bota'},{'city':'Changkat Jering'},{'city':'Changkat Keruing'},{'city':'Chemor'},{'city':'Chenderiang'},{'city':'Chenderong Balai'},{'city':'Chikus'},{'city':'Enggor'},{'city':'Gerik'},{'city':'Gopeng'},{'city':'Hutan Melintang'},{'city':'Intan'},{'city':'Ipoh'},{'city':'Jeram'},{'city':'Kampar'},{'city':'Kampong Gajah'},{'city':'Kampong Kepayang'},{'city':'Kamunting'},{'city':'Kuala Kangsar'},{'city':'Kuala Kurau'},{'city':'Kuala Sepetang'},{'city':'Lahat'},{'city':'Lambor Kanan'},{'city':'Langkap'},{'city':'Lenggong'},{'city':'Lumut'},{'city':'Malim Nawar'},{'city':'Mambang Diawan'},{'city':'Manong'},{'city':'Matang'},{'city':'Menglembu'},{'city':'Padang Rengas'},{'city':'Pangkor'},{'city':'Pantai Remis'},{'city':'Parit'},{'city':'Parit Buntar'},{'city':'Pengkalan Hulu'},{'city':'Pusing'},{'city':'Rantau Panjang'},{'city':'Sauk'},{'city':'Selama'},{'city':'Selekoh'},{'city':'Selinsing'},{'city':'Semanggol'},{'city':'Seri Manjong'},{'city':'Simpang'},{'city':'Sitiawan'},{'city':'Slim River'},{'city':'Sungai Siput'},{'city':'Sungai Sumun'},{'city':'Sungkai'},{'city':'Taiping'},{'city':'Tanjong Piandang'},{'city':'Tanjong Rambutan'},{'city':'Tanjong Tualang'},{'city':'Tanjung Malim'},{'city':'Tapah'},{'city':'Teluk Intan'},{'city':'Temoh'},{'city':'TLDM Lumut'},{'city':'Trolak'},{'city':'Trong'},{'city':'Tronoh'},{'city':'Ulu Bernam'},{'city':'Ulu Kinta'}]},{'id':'PL','name':'Perlis','cities':[{'city':'Arau'},{'city':'Kaki Bukit'},{'city':'Kangar'},{'city':'Kuala Perlis'},{'city':'Padang Besar'},{'city':'Pauh'},{'city':'Simpang Ampat'}]},{'id':'PJ','name':'Putrajaya','cities':[{'city':'Cyberjaya'},{'city':'Putrajaya'}]},{'id':'SA','name':'Sabah','cities':[{'city':'Beaufort'},{'city':'Beluran'},{'city':'Bongawan'},{'city':'Keningau'},{'city':'Kota Belud'},{'city':'Kota Kinabalu'},{'city':'Kota Kinabatangan'},{'city':'Kota Marudu'},{'city':'Kuala Penyu'},{'city':'Kudat'},{'city':'Kunak'},{'city':'Lahad Datu'},{'city':'Likas'},{'city':'Membakut'},{'city':'Menumbok'},{'city':'Nabawan'},{'city':'Pamol'},{'city':'Papar'},{'city':'Penampang'},{'city':'Pitas'},{'city':'Pulatan'},{'city':'Ranau'},{'city':'Sandakan'},{'city':'Semporna'},{'city':'Sipitang'},{'city':'Tambunan'},{'city':'Tamparuli'},{'city':'Tawau'},{'city':'Tenom'},{'city':'Tuaran'}]},{'id':'SW','name':'Sarawak','cities':[{'city':'Asajaya'},{'city':'Balingian'},{'city':'Baram'},{'city':'Bau'},{'city':'Bekenu'},{'city':'Belaga'},{'city':'Belawai'},{'city':'Betong'},{'city':'Bintagor'},{'city':'Bintulu'},{'city':'Dalat'},{'city':'Daro'},{'city':'Debak'},{'city':'Engkilili'},{'city':'Julau'},{'city':'Kabong'},{'city':'Kanowit'},{'city':'Kapit'},{'city':'Kota Samarahan'},{'city':'Kuching'},{'city':'Lawas'},{'city':'Limbang'},{'city':'Lingga'},{'city':'Long Lama'},{'city':'Lubok Antu'},{'city':'Lundu'},{'city':'Lutong'},{'city':'Maradong'},{'city':'Marudi'},{'city':'Matu'},{'city':'Miri'},{'city':'Mukah'},{'city':'Nanga Medamit'},{'city':'Niah'},{'city':'Pusa'},{'city':'Roban'},{'city':'Saratok'},{'city':'Sarikei'},{'city':'Sebauh'},{'city':'Sebuyau'},{'city':'Serian'},{'city':'Sibu'},{'city':'Simunjan'},{'city':'Song'},{'city':'Spaoh'},{'city':'Sri Aman'},{'city':'Sundar'},{'city':'Tanjung Kidurong'},{'city':'Tatau'}]},{'id':'SE','name':'Selangor','cities':[{'city':'Alam Impian'},{'city':'Aman Perdana'},{'city':'Ambang Botanic'},{'city':'Ampang'},{'city':'Ara Damansara'},{'city':'Balakong'},{'city':'Bandar Botanic'},{'city':'Bandar Bukit Raja'},{'city':'Bandar Bukit Tinggi'},{'city':'Bandar Kinrara'},{'city':'Bandar Puncak Alam'},{'city':'Bandar Puteri Klang'},{'city':'Bandar Puteri Puchong'},{'city':'Bandar Saujana Putra'},{'city':'Bandar Sri Damansara'},{'city':'Bandar Sungai Long'},{'city':'Bandar Sunway'},{'city':'Bandar Utama'},{'city':'Bangi'},{'city':'Banting'},{'city':'Batang Berjuntai'},{'city':'Batang Kali'},{'city':'Batu Arang'},{'city':'Batu Caves'},{'city':'Beranang'},{'city':'Bukit Jelutong'},{'city':'Bukit Rahman Putra'},{'city':'Bukit Rotan'},{'city':'Bukit Subang'},{'city':'Cheras'},{'city':'Country Heights'},{'city':'Cyberjaya'},{'city':'Damansara Damai'},{'city':'Damansara Intan'},{'city':'Damansara Jaya'},{'city':'Damansara Kim'},{'city':'Damansara Perdana'},{'city':'Damansara Utama'},{'city':'Denai Alam'},{'city':'Dengkil'},{'city':'Glenmarie'},{'city':'Gombak'},{'city':'Hulu Langat'},{'city':'Hulu Selangor'},{'city':'Jade Hills'},{'city':'Jenjarom'},{'city':'Kajang'},{'city':'Kapar'},{'city':'Kayu Ara'},{'city':'Kelana Jaya'},{'city':'Kerling'},{'city':'Klang'},{'city':'Kota Damansara'},{'city':'Kota Emerald'},{'city':'Kota Kemuning'},{'city':'Kuala Kubu Baru'},{'city':'Kuala Langat'},{'city':'Kuala Selangor'},{'city':'Kuang'},{'city':'Mutiara Damansara'},{'city':'Nilai'},{'city':'Petaling Jaya'},{'city':'Port Klang'},{'city':'Puchong'},{'city':'Puchong South'},{'city':'Pulau  Indah ( Pulau Lumut)'},{'city':'Pulau Carey '},{'city':'Pulau Ketam'},{'city':'Puncak Jalil'},{'city':'Putra Heights'},{'city':'Rasa'},{'city':'Rawang'},{'city':'Sabak Bernam'},{'city':'Saujana'},{'city':'Sekinchan'},{'city':'Selayang'},{'city':'Semenyih'},{'city':'Sepang'},{'city':'Serdang'},{'city':'Serendah'},{'city':'Seri Kembangan'},{'city':'Setia Alam'},{'city':'Setia Eco Park'},{'city':'Shah Alam'},{'city':'SierraMas'},{'city':'SS2'},{'city':'Subang Bestari'},{'city':'Subang Heights'},{'city':'Subang Jaya'},{'city':'Sungai Ayer Tawar'},{'city':'Sungai Besar'},{'city':'Sungai Buloh'},{'city':'Sungai Pelek'},{'city':'Taman Melawati'},{'city':'Taman TTDI Jaya'},{'city':'Tanjong Karang'},{'city':'Tanjong Sepat'},{'city':'Telok Panglima Garang'},{'city':'Tropicana'},{'city':'Ulu Klang'},{'city':'USJ'},{'city':'USJ Heights'},{'city':'Valencia'}]},{'id':'TR','name':'Terengganu','cities':[{'city':'Besut'},{'city':'Dungun'},{'city':'Hulu Terengganu'},{'city':'Kemaman'},{'city':'Kuala Terengganu'},{'city':'Marang'},{'city':'Setiu'},{'city':'Kerteh'}]}]}";
		
		Gson gson = new Gson();
		Country obj = gson.fromJson(countryObj, Country.class);
		
		return obj;
	}
		
	
	public static String downloadPhotos(String downloadUrl, String imageName){
		String status="";
		// Image img = new Image();
				URL imgUrl = null;
				try {
					imgUrl = new URL(downloadUrl);
					//File imgLocation = new File("G:\\Hadoop_2015\\Crawler_images\\"+imageName+".jpg");
					File imgLocation = new File("/home/hadoop/propertyImages/"+imageName+".jpg");
					//s3://hadoopwebcrawler1
					FileUtils.copyURLToFile(imgUrl, imgLocation);
				} catch (Exception e) {
					System.out.println("---Exception Occured While downloading image. Image URl ==>" + imgUrl);
				}
		
		return status;
	}
	
	
	//For testing individual methods -
	
	
		/*String url = "http://homes.mitula.my/homes/condominium-damansara-perdana";
		String commaSeperatedLstOfUrls = "Initialized,NoLinkAdded";
		System.out.println("position Mapper 1");
		//commaSeperatedLstOfUrls = spider.getAllUrls(url);
		 List<PropertyDetails> allPropoertiesLst = spider.getAllPropertiesDtls(url);
		System.out.println("All properties list creation Done . No of Urls added -"
				+ commaSeperatedLstOfUrls.split("\n").length);*/

	/*	try {
			PrintWriter writer = new PrintWriter("d:\\properties.txt", "UTF-8");
			writer.println(commaSeperatedLstOfUrls);
			writer.close();
		} catch (Exception e) {
			System.out.println("Exception occured in Writing file to FS -- " + e);
		}*/
		
		//--- code for propertyDetails convert from object to json.
	/*	Gson gson = new Gson();
		String json = gson.toJson(allPropoertiesLst);
		try {
			PrintWriter writer = new PrintWriter("d:\\thinkProperty.txt", "UTF-8");
			writer.println(json);
			writer.close();
		} catch (Exception e) {
			System.out.println("Exception occured in Writing file to FS -- " + e);
		}*/
		
		// ----- code for propertyDetails convert from object to json.--END
		
		//  SpiderLeg sl = new SpiderLeg(); sl.downloadImages();
		 //--
			// --WORKING---http://www.propertyguru.com.my/
			// --WORKING-------http://homes.trovit.my/
			// --WORKING-------http://thinkproperty.com.my/
			
			// WORKING----http://www.propwall.my/
			// --WORKING-------http://homes.mitula.my/
			// --WORKING-------http://property.malaysiamostwanted.com/ use
			// --http://property.malaysiamostwanted.com/show/search?q=malaysia&page=2
			// -- MAP ISSUE -- NOT WORKING--http://www.starproperty.my --
			// --INCAPSULA ---NOT WORKING
			// --http://www.iproperty.com.my/property/searchresult.aspx?t=S&gpt=AR&st=&ct=&k=&pt=&mp=&xp=&mbr=&xbr=&mbu=&xbu=&lo=&wp=&wv=&wa=&ht=&au=&ft=&sby=&ns=1
	}

