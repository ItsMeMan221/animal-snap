-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 11, 2023 at 11:01 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `animals` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL,
  `animal_picture` varchar(255) NOT NULL,
  `habitat_id` varchar(255) NOT NULL,
  `donasi_id` varchar(255) DEFAULT NULL,
  `class_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `animals`
--

INSERT INTO `animals` (`id`, `nama`, `deskripsi`, `animal_picture`, `habitat_id`, `donasi_id`, `class_id`, `status_id`) VALUES
(0, 'Arctic Fox (Rubah arktik)', 'Rubah arktik memiliki bulu tebal berwarna putih untuk menjaga tubuhnya agar tetap hangat ketika habitatnya mencapai suhu 104 derajat Farenheit. Ekor panjang dan halus digunakan oleh rubah arktik untuk dijadikan selimut yang membungkus sekitar tubuhnya saat tidur. Kaki mereka juga terdiri dari lapisan bulu tebal hingga dapat meredam suara langkah kakinya ketika berburu. Rubah arktik merupakan hewan omnivora yang memakan tikus kecil yang disebut dengan lemmings, namun pada masa-masa sulit, mereka biasanya memakan apapun yang ditemukan seperti serangga, buah, beri, dan bahkan kotoran hewan lain. Saat tidak menemukan makanan, biasanya mereka menggali sarang salju dan melindungi tubuhnya selama 2 minggu untuk menjaga tubuhnya tetap hangat. Sarang tersebut membantu mereka dalam memperlambat detak jantung dan metabolisme sehingga dapat menghemat energi. Rubah arktik dapat hidup dalam rentang 3 sampai 6 tahun lamanya di alam liar. ', 'https://storage.googleapis.com/animal-snap-user-images/animal/arctic%20fox.jpg', '11', NULL, 5, 3),
(1, 'Butterfly (Kupu-kupu)', 'Kupu-kupu adalah salah satu makhluk yang indah dengan pola yang menakjubkan di sayapnya. Tubuh kupu-kupu terdiri dari kepala, dada, dan perut. Tubuhnya dilapisi oleh rambut sensorik kecil dan memiliki dua pasang sayap serta enam kaki yang melekat pada bagian dada. Sayapnya tertutupi oleh sisik-sisik berwarna-warni dan kupu-kupu hanya bisa terbang ketika tubuhnya hangat, sehingga seringkali mereka berjemur untuk menghangatkan diri. Habitat kupu-kupu terutama terdapat di daerah tropis, terutama di hutan hujan tropis. Kupu-kupu mengalami siklus hidup yang disebut metamorfosis, dimulai dari telur, larva, pupa, hingga menjadi kupu-kupu dewasa.\r\n', 'https://storage.googleapis.com/animal-snap-user-images/animal/butterfly.jpeg', '2', NULL, 6, 3),
(2, 'Cat (Kucing)', 'Kucing adalah salah satu spesies yang sering dijadikan hewan peliharaan dalam keluarga dan biasanya disebut sebagai kucing domestik atau kucing rumah. Mereka terkenal dengan kemampuan mereka dalam memburu tikus kecil. Kucing memiliki tubuh yang fleksibel dan kuat, refleks yang cepat, gigi yang tajam, dan cakar yang digunakan untuk menangkap mangsanya. Mereka juga memiliki penglihatan yang luar biasa, mampu melihat dengan tingkat cahaya hanya sepersen dari yang dibutuhkan oleh manusia. Reproduksi pada kucing melibatkan proses kelahiran, menjadikannya sebagai salah satu spesies hewan mamalia.\r\n', 'https://storage.googleapis.com/animal-snap-user-images/animal/cat.jpeg', '1,7', NULL, 5, 3),
(3, 'Cheetah', 'Cheetah merupakan mamalia tercepat yang hidup di darat karena kecepatannya mencapai 60 sampai 70 mil (97 sampai 113 kilometer) per jam dalam jarak pendek. Namun ketika mengejar mangsanya, cheetah hanya menggunakan setengah kecepatannya dan membutuhkan sekitar setengah jam untuk mengatur napasnya kembali setelah pengejerannya. Anggota tubuh cheetah yang panjang dan ramping dengan bantalan kaki yang keras serta tulang belakang yang fleksibel dapat membantu mereka untuk mencapai kecepatan tertinggi dengan langkahnya yang panjang. Permukan tubuh cheetah yang berwarna kecoklatan dan dipenuhi bintik-bintik hitam menjadi salah satu bentuk penyamarannya dalam berburu mangsa di padang rumput yang tinggi dan kering. Garis-garis hitam tebal yang ada dari sudut mata hingga ke kedua sisi mulut mereka dan ujung ekor yang memiliki cincin hitam merupakan salah satu ciri khas bentuk cheetah. Cheetah merupakan hewan karnivora dan hidup di padang rumput terbuka. Jumlah mangsa yang meningkat dan hilangnya habitat membuat cheetah menjadi salah satu hewan yang terancam punah.', 'https://storage.googleapis.com/animal-snap-user-images/animal/cheetah.jpeg', '1', '3,4,5', 5, 2),
(4, 'Chicken (Ayam)', 'Ayam adalah jenis unggas dengan kepala yang lebih kecil dibandingkan dengan tubuhnya. Mereka memiliki paruh, sayap yang pendek, tubuh yang bulat, serta bertengger dengan kaki yang tidak memiliki bulu. Ayam memiliki empat cakar di setiap kakinya yang digunakan untuk menggali tanah mencari makanan. Perbedaan antara ayam jantan dan betina dapat dilihat dari ukuran tubuhnya, di mana ayam jantan cenderung memiliki badan yang lebih besar. Selain itu, ayam jantan juga biasanya memiliki suara yang panjang dan melengking. Ayam berkembang biak dengan cara bertelur dan mereka termasuk hewan omnivora, artinya mereka dapat memakan berbagai jenis makanan. Ayam sering dijadikan sumber pangan bagi manusia dan digunakan sebagai bahan makanan yang lezat. ', 'https://storage.googleapis.com/animal-snap-user-images/animal/chicken.jpeg', '1', NULL, 4, 3),
(5, 'Chimpanzee (simpanse)', 'Simpanse memiliki bentuk yang khas, yaitu lengan yang mencapai lutut, ibu jari yang berlawanan, dan mulut yang menonjol. Bagian tubuh tertentu yang tidak ditutupi seperti kulit wajah, telingan, telapak tangan, dan telapak kaki menjadi salah satu ciri fisik dari simpanse. Simpanse memiliki siklus kehidupan yang unik seperti bayi simpanse yang menempel pada perut ibunya saat bepergian. Setelah itu, simpanse muda akan mengamati dan belajar dari ibunya selama 7 hingga 10 tahun untuk belajar merawat, membuat sarang, dan mencari makanan. Simpanse berjalan dengan merangkak (knuckle-walking) atau dengan berayun dari cabang pohon satu ke cabang pohon lainnya. Simpanse merupakan hewan omnivora yang memakan tumbuhan dan hewan. Simpanse juga memiliki rentang hidup cukup lama di alam liar hingga mencapai 45 tahun. Namun, simpanse terancam punah dikarenakan perdagangan satwa liar secara ilegal melalui pemburuan dan penyakit wabah ebola yang membunuh puluhan ribu ', 'https://storage.googleapis.com/animal-snap-user-images/animal/chimpanzee.jpeg', '2', '1,2', 5, 1),
(6, 'Cow (Sapi)', 'Sapi adalah salah satu jenis hewan ternak terbesar kedua di dunia berdasarkan populasi mereka. Di seluruh dunia, populasi sapi mencapai 1,5 miliar ekor. Sapi memiliki berat antara 700 hingga 1760 pon dan mampu menghasilkan lebih dari tujuh galon susu setiap harinya. Mereka adalah hewan herbivora yang mengkonsumsi rumput, gandum, biji-bijian, dan tepung kanola sebagai makanan mereka.Salah satu ciri khas sapi adalah variasi pola tubuh yang bervariasi, tergantung pada kombinasi kulit dan rambut yang dimilikinya. Sapi hidup dalam kelompok yang disebut kine, di mana mereka saling berinteraksi dan berkomunikasi satu sama lain.', 'https://storage.googleapis.com/animal-snap-user-images/animal/cow.jpeg', '1,4', NULL, 5, 3),
(7, 'Dog (Anjing)', 'Anjing, keturunan serigala yang telah dijinakkan, merupakan salah satu hewan yang memiliki banyak jenis. Sebagai hewan pertama yang dijinakkan oleh manusia prasejarah, anjing telah menjadi bagian tak terpisahkan dari kehidupan manusia. Anjing merupakan hewan karnivora yang menyukai makanan seperti ayam dan daging lainnya. Dengan gigi-gigi khususnya, mereka dapat menggigit dan merobek makanan dengan mudah. Anjing juga sering dijadikan sebagai hewan peliharaan yang menemani manusia sesuai dengan peran dan fungsinya, seperti anjing penjaga, pemburu, dan lain-lain. Saat ini, populasi anjing diperkirakan mencapai 471 juta di seluruh dunia.', 'https://storage.googleapis.com/animal-snap-user-images/animal/dog.jpeg', '1,4', NULL, 5, 3),
(8, 'Elephant (Gajah)', 'Gajah adalah salah satu hewan paling cerdas di dunia. Mereka memiliki kemampuan mengingat detail, belajar, dan merasakan emosi dengan mendalam. Gajah memiliki tubuh yang besar dengan kaki yang kuat, ekor yang tipis, telinga yang bulat, belalai yang kuat, dan gading. Gading gajah digunakan untuk mencari makanan dan air, serta sebagai alat pertahanan dan mengangkat beban berat. Sementara itu, belalai gajah berfungsi untuk penciuman, komunikasi, dan sentuhan.Gajah memiliki tinggi sekitar 10 kaki, panjang tubuh antara 18 hingga 24 kaki, dan beratnya dapat mencapai 4 hingga 8 ton. Mereka juga memiliki pendengaran yang luar biasa, mampu mendengar panggilan gajah lain dalam jarak sekitar 2,5 mil. Gajah hidup dalam kelompok sosial yang disebut kawanan, dan saat ini populasinya sangat terancam dengan jumlah kurang dari 500.000 individu.', 'https://storage.googleapis.com/animal-snap-user-images/animal/elephant.jpeg', '1', NULL, 5, 2),
(9, 'Hamster', 'Hamster adalah hewan kecil yang sering dijadikan hewan peliharaan di rumah. Mereka memiliki tubuh seperti tikus kecil dengan kaki yang lebar, telinga kecil, dan ekor pendek. Bulu mereka lembut dan panjang, dengan panjang tubuh sekitar 2 hingga 4 inci. Gigi hamster akan terus tumbuh sepanjang waktu dan akan memendek saat mereka mengunyah makanan. Ketika merasa takut atau terancam, hamster mungkin akan menggigit. Selama tidur, ibu hamster akan sangat protektif dan menjaga bayinya di dalam kantong mulutnya jika merasa ada ancaman. Hamster adalah hewan herbivora yang makanannya meliputi biji-bijian, kacang-kacangan, dan sayuran.', 'https://storage.googleapis.com/animal-snap-user-images/animal/hamster.jpeg', '1', NULL, 5, 3),
(10, 'Horse (Kuda)', 'Kuda merupakan hewan yang memiliki tubuh yang kuat dengan leher panjang yang mampu menopang kepalanya yang besar dan panjang. Mereka memiliki mata yang lebih besar dibandingkan dengan mamalia darat lainnya. Telinga kuda juga tergolong besar, ditutupi dengan rambut yang panjang yang tumbuh sepanjang leher, serta memiliki ekor pendek yang juga dilapisi oleh rambut.Kuda memiliki penglihatan yang sangat baik, hampir mencapai sudut pandang 360 derajat, dan indra penciuman mereka juga jauh lebih tajam daripada manusia. Ketika hidup dalam kelompok, kuda berkomunikasi satu sama lain terutama melalui bahasa tubuh. Saat merasa terancam, mereka dapat menendang, memukul, atau menggigit sebagai mekanisme pertahanan diri.Sebagai hewan herbivora, kuda memakan rumput sebagai sumber utama makanan mereka. ', 'https://storage.googleapis.com/animal-snap-user-images/animal/horse.jpg', '1', NULL, 5, 3),
(11, 'Sheep (Domba)', 'Domba adalah salah satu hewan peliharaan yang sangat sukses di seluruh dunia. Mereka memiliki ciri-ciri yang khas, seperti kuku yang kuat dengan empat kaki, tubuh yang gemuk, dan ekor yang pendek. Bulu domba sangat lebat dan terdiri dari dua lapisan, yaitu bulu luar yang tebal dan bulu dalam yang lembut. Domba jantan memiliki tanduk yang lebih besar dibandingkan domba betina.Domba hidup dalam kelompok dan sangat sosial. Mereka tidak suka terpisah dari kawanan mereka. Domba adalah hewan herbivora, yang berarti mereka memakan makanan tumbuhan. Mereka biasanya memakan rumput, daun, kacang-kacangan, dan tanaman berbunga. ', 'https://storage.googleapis.com/animal-snap-user-images/animal/horse.jpg', '1,4', NULL, 5, 3),
(12, 'Spider (Laba-laba)', 'Laba-laba adalah makhluk serangga yang memiliki ciri khas delapan kaki dan taring yang dapat menyuntikkan racun. Mereka juga memiliki organ khusus yang disebut pemintal yang berfungsi untuk mengeluarkan sutra. Pemintal ini terletak pada kaki depan atau bagian bawah perut mereka. Dengan menggunakan sutra ini, laba-laba dapat membuat jaring yang digunakan untuk berbagai tujuan, seperti mencari makan, menangkap mangsa, membuat perlindungan diri, dan lain-lain. Yang menarik, laba-laba berbeda dengan serangga lainnya karena mereka tidak memiliki antena. Makanan utama laba-laba adalah lalat, lebah, dan semut.', 'https://storage.googleapis.com/animal-snap-user-images/animal/spider.jpeg', '1,2,7', NULL, 6, 3),
(13, 'Squirrel (Tupai)', 'Tupai adalah hewan yang memiliki tubuh yang lincah dan gesit. Mereka memiliki tubuh yang ramping dengan mata yang besar dan ekor yang lebat, meskipun variasi ekor ini tergantung pada spesiesnya. Kebanyakan spesies tupai memiliki telinga kecil yang runcing dan cakar yang kecil dan tajam, memudahkan mereka dalam memanjat pohon dengan cepat. Berat tubuh tupai berkisar antara 1 hingga 1,5 pon.Tupai dikenal sebagai hewan yang cerdas dan mudah bergaul dengan manusia. Ketika merasa terancam, mereka dengan cepat akan memanjat kembali ke pohon sebagai tindakan perlindungan. Selain itu, tupai juga sering mengibaskan ekornya sebagai peringatan adanya bahaya.Tupai memiliki kebiasaan menyimpan makanan seperti kacang-kacangan dan biji-bijian di berbagai tempat yang berbeda. Mereka merupakan hewan omnivora yang makanannya meliputi biji-bijian, kacang-kacangan, jamur, buah, dan serangga kecil.', 'https://storage.googleapis.com/animal-snap-user-images/animal/squirrel.jpeg', '2', NULL, 5, 3);

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

CREATE TABLE `class` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`id`, `nama`, `deskripsi`) VALUES
(1, 'Pisces', 'Ikan adalah kelompok hewan bertulang belakang (vertebrata) yang memiliki sirip, bernafas menggunakan insang, dan biasanya hidup di dalam air. Kelompok ikan vertebrata ini berbeda dengan kelompok hewan air lainnya karena ikan memiliki ciri umum seperti tubuh yang biasanya dilapisi dengan sisik, memiliki sirip untuk berenang, sistem pernafasan yang menggunakan insang, dan memiliki habitat di dalam air.'),
(2, 'Amphibi', 'Kata amphibi berasal dari kata Amphi yang berarti ganda dan Bios yang berarti hidup sehingga dapat diartikan bahwa amphibi merupakan hewan yang hidup dalam dua habitat, yaitu air dan darat. Amphibi berkembang biak dengan cara bertelur (ovipar) dan bernapas menggunakan insang atau paru-paru.'),
(3, 'Reptil', 'Reptil berasal dari bahasa Latin yang memiliki arti melata. Tubuh reptil umumnya ditutupi oleh sisik tanduk (keratin) untuk mencegah kekeringan dan bernapas menggunakan paru-paru. Reptil berkembang biak dengan cara bertelur (ovipar).'),
(4, 'Aves', 'Nama lain dari Aves adalah burung. Aves merupakan hewan yang seluruh tubuhnya ditutupi oleh bulu yang membentuk sayang sehingga memiliki kemampuan untuk terbang. Aves berkembang biak dengan cara bertelur dan bernapas melalui paru-paru yang memiliki pundi-pundi udara. '),
(5, 'Mamalia', 'Mamalia merupakan hewan vertebrata yang berdarah panas atau homoetherm. Mamalia memiliki ciri khas dibandingkan dengan hewan vertebrata lainnya, yaitu mempunyai kelenjar susu yang digunakan untuk menyusui dan berkembang biak dengan cara melahirkan (vivipar). Tubuh mamalia ditutupi oleh rambut dan bernapas menggunakan paru-paru.'),
(6, 'Insekta', 'Insekta atau serangga memiliki tubuh yang terdiri dari tiga bagian, yaitu kepala, dada, dan perut. Serangga merupakan kelompok hewan yang sangat beragam, dengan perkiraan jumlah spesies antara enam hingga sepuluh juta. Mereka dapat ditemukan hampir di semua jenis lingkungan dan mengalami proses perkembangan melalui telur. Siklus hidup serangga disebut metamorfosis. Meskipun beberapa serangga dianggap sebagai hama oleh manusia, sehingga insektisida digunakan untuk membunuhnya.');

-- --------------------------------------------------------

--
-- Table structure for table `donasi`
--

CREATE TABLE `donasi` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `donasi`
--

INSERT INTO `donasi` (`id`, `nama`, `link`) VALUES
(1, 'World Wild Life (WWF)', 'https://www.worldwildlife.org/species/chimpanzee'),
(2, 'Ngamba Island', 'https://ngambaisland.org'),
(3, 'ZSL Organization', 'https://www.zsl.org/what-we-do/projects/cheetah-conservation-africa'),
(4, 'People\'s Trust For Endangered Species', 'https://ptes.org/grants/worldwide-projects/cheetah-conservation-in-botswana/'),
(5, 'Hoedspruit Endangered Species Centre', 'https://hesc.co.za/donations/');

-- --------------------------------------------------------

--
-- Table structure for table `habitat`
--

CREATE TABLE `habitat` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `gambar` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `habitat`
--

INSERT INTO `habitat` (`id`, `nama`, `gambar`, `deskripsi`) VALUES
(1, 'Padang rumput', 'https://storage.googleapis.com/animal-snap-user-images/habitat/padang%20rumput.jpg', 'Habitat padang rumuput didominasi oleh rumput yang merupakan tanaman berbunga keluarga Poaceae. Padang rumuput merupakan tempat terbuka yang jarang pohon sehingga memiliki tanah yang kering. Selain itu, curah hujan yang relatif rendah mengakibatkan kurangnya keanekaragaman hayati dan rentan terhadap kebakaran. '),
(2, 'Hutan hujan tropis', 'https://storage.googleapis.com/animal-snap-user-images/habitat/hutan%20hujan%20tropis.jpg', 'Hutan hujan tropis merupakan lahan basah dan hangat yang tumbuh di daerah sekitar khatulistiwa. Lokasi hutan ini menjadikan habitat ini menjadi salah satu temapyt keanekaragaman hayati paling tinggi dari habitat darat lainnya. Habitat ini paling sering ditemukan di dunia. '),
(3, 'Gurun ', 'https://storage.googleapis.com/animal-snap-user-images/habitat/gurun.jpg', 'Gurun merupakan daerah kering luas dengan curah hujan yang sangat sedikit. Daerah ini memiliki tingkat vegetasi paling rendah atau bahkan tanpa vegetasi. Suhu udara yang ekstrim merupakan salah satu ciri gurun sehingga hewan yang tinggal memiliki kemampuan adaptif hidup tanpa air. '),
(4, 'Pegunungan', 'https://storage.googleapis.com/animal-snap-user-images/habitat/pegunungan.jpg', 'Habitat gunung biasanya mengacu pada medan di atas kaki bukit yang terbentuk akibat gerakan tektonik. Ketersediaan makanan yang rendah dan iklim yang relatif dingin membuat hewan yang hidup pada habitat ini memiliki kemampuan yang dapat beradaptasi dengan lingkungan tersebut. '),
(5, 'Sungai dan aliran', 'https://storage.googleapis.com/animal-snap-user-images/habitat/sungai%20dan%20aliran.jpg', 'Habitat sungai biasanya ditandai dengan air yang mengalir. Banyak spesies hewan air yang hidup pada habitat ini seperti ikan. Selain itu, badan sungai dan air sungai banyak dijadikan tempat sekaligus mata air bagi berbagai macam hewan.'),
(6, 'Danau dan kolam', 'https://storage.googleapis.com/animal-snap-user-images/habitat/danau%20dan%20kolam.jpg', 'Habitat danau merupakan air yang tenang berbeda dengan sungai yang memiliki aliran atau arus air. Danau dapat terbentuk akibat aktivitas vulkanik, glasial, dan tektonik. Habitat ini biasanya merupakan habitat bagi hewan air tawar.'),
(7, 'Lahan basah', 'https://storage.googleapis.com/animal-snap-user-images/habitat/lahan%20basah.jpg', 'Lahan basah merupakan habitat dimana tanah yang tergenang air atau banjir, biasanya terdapat di muara sungai, tepi danau atau kolam, rawa-rawa. Tanaman yang tumbuh biasanya berupa tanaman air dan pohon kayu. '),
(8, 'Air payau', 'https://storage.googleapis.com/animal-snap-user-images/habitat/air%20payau.jpg', 'Air payau merupakan campuran dari air asin dan air tawar. Badan air ini terbentuk dari pertemuan antara air tawar dan air laut yang berada di tempat seperti muara, teluk, dan lainnya. '),
(9, 'Laut terbuka', 'https://storage.googleapis.com/animal-snap-user-images/habitat/laut%20terbuka.jpg', 'Laut terbuka merupakan daerah perairan laut yang kedalamannya dimulai dari 500 hingga 1000 meter. Daerah ini juga sering disebut dengan zona pelagis yang banyak dihuni oleh mamalia hewan laut. '),
(10, 'Terumbu Karang', 'https://storage.googleapis.com/animal-snap-user-images/habitat/terumbu%20karang.jpg', 'Terumbu karang merupakan struktur yang terbentuk dari hasil sekresi kalsium karbonat. Secara umum, terumbu karang berbentuk seperti karang batuan dang membentuk polip secara berkelompok. Habitat ini menjadi salah satu tempat bagi hewan kecil dan diperkirakan 25 persen dari spesies laut hidup dalma terumbu karang. '),
(11, 'Kutub Utara', 'https://storage.googleapis.com/animal-snap-user-images/habitat/kutub-utara.jpg', 'Kutub Utara merupakan titik paling utara bumi yang dilalui oleh garid khayal 90 derajat Lintang Utara. Kutub Utara berada di Samudra Arktik, di atas air yang hampir selalu tertutup es. Ketebalan es di kutub utara berkisar dari 2 hingga 3 meter (6 sampai 10 kaki). Kedalaman laut yang ada di sana mencapai 4000 meter dalamnya. Pada saat musim panas, suhu di kutub utara dapat mencapai titik beku, yaitu 0 derajat Celcius, namun kutub utara jauh lebih hangat daripada kutub selatan karena ketinggiannya yang lebih rendah dan terletak di tengah lautan.');

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE `status` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`id`, `nama`) VALUES
(1, 'Terancam Punah'),
(2, 'Rentan'),
(3, 'Rendah');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `uid` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `date_joined` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_classify`
--

CREATE TABLE `user_classify` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `animal_id` int(11) NOT NULL,
  `date_classified` varchar(255) NOT NULL,
  `animal_image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animals`
--
ALTER TABLE `animals`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `donasi`
--
ALTER TABLE `donasi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `habitat`
--
ALTER TABLE `habitat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_classify`
--
ALTER TABLE `user_classify`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `class`
--
ALTER TABLE `class`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `donasi`
--
ALTER TABLE `donasi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `habitat`
--
ALTER TABLE `habitat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `status`
--
ALTER TABLE `status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
