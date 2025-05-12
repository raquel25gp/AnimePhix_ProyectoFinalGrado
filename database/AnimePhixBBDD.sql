CREATE DATABASE IF NOT EXISTS bbddAnimePhix;
USE bbddAnimePhix;

-- Creación de la tabla roles
CREATE TABLE IF NOT EXISTS rol (
	idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL UNIQUE
);

-- Creación de la tabla usuario
CREATE TABLE IF NOT EXISTS usuario (
	idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    urlImagen VARCHAR(200),
    habilitado BOOLEAN NOT NULL DEFAULT true,
    rolId INT,
    CONSTRAINT fk_rolUsuarioId FOREIGN KEY (rolId) REFERENCES rol(idRol) ON DELETE CASCADE
);

-- Creación de la tabla generos
CREATE TABLE IF NOT EXISTS genero (
	idGenero INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(100) NOT NULL
);

-- Creación de la tabla estados
CREATE TABLE IF NOT EXISTS estado (
	idEstado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

-- Creación de la tabla anime
CREATE TABLE IF NOT EXISTS anime (
	idAnime INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(5000),
    fechaInicio DATE,
    fechaFin DATE,
    diaSemana INT,
    visible BOOLEAN NOT NULL DEFAULT true,
    urlImagen VARCHAR(500),
    generoId INT,
    estadoId INT,
    CONSTRAINT fk_generoAnimeId FOREIGN KEY (generoId) REFERENCES genero(idGenero) ON DELETE CASCADE,
    CONSTRAINT fk_estadoAnimeId FOREIGN KEY (estadoId) REFERENCES estado(idEstado) ON DELETE CASCADE
);

-- Creación de la tabla episodio
CREATE TABLE IF NOT EXISTS episodio (
	animeId INT NOT NULL,
    numEpisodio INT NOT NULL,
    urlVideo VARCHAR(500),
    fechaLanzamiento DATE,
    urlPoster VARCHAR(500),
    PRIMARY KEY (animeId, numEpisodio),
    CONSTRAINT fk_animeEpisodio_id FOREIGN KEY (animeId) REFERENCES anime(idAnime) ON DELETE CASCADE
);

-- Creación de la tabla intermedia marcar_favoritos
CREATE TABLE IF NOT EXISTS favoritoAnimeUsuario (
	usuarioId INT NOT NULL,
    animeId INT NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (usuarioId, animeId),
    CONSTRAINT fk_usuarioFav_id FOREIGN KEY (usuarioId) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_animeFav_id FOREIGN KEY (animeId) REFERENCES anime(idAnime) ON DELETE CASCADE
);

-- Creación de la tabla intermedia marcar_episodios_vistos
CREATE TABLE IF NOT EXISTS vistoEpisodioUsuario (
	usuarioId INT NOT NULL,
    animeId INT NOT NULL,
    numEpisodio INT NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (usuarioId, animeId, numEpisodio),
    CONSTRAINT fk_usuarioVisto_id FOREIGN KEY (usuarioId) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_episodioVisto_id FOREIGN KEY (animeId, numEpisodio) REFERENCES episodio(animeId, numEpisodio) ON DELETE CASCADE
);

-- Creación de la tabla intermedia comentar_episodio
CREATE TABLE IF NOT EXISTS comentarEpisodio (
	idComentario INT AUTO_INCREMENT PRIMARY KEY,
	usuarioId INT NOT NULL,
    animeId INT NOT NULL,
    numEpisodio INT NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT true,
    comentario VARCHAR(1000),
    fechaCreacion DATETIME,
    CONSTRAINT fk_usuarioComenta_id FOREIGN KEY (usuarioId) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_episodioComenta_id FOREIGN KEY (animeId, numEpisodio) REFERENCES episodio(animeId, numEpisodio) ON DELETE CASCADE
);

-- Creación de la tabla tiposProblemas
CREATE TABLE IF NOT EXISTS tipoProblema (
	idTipoProblema INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Creación de la tabla reporte
CREATE TABLE IF NOT EXISTS reporte (
	id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(1500) NOT NULL,
    corregido BOOLEAN NOT NULL DEFAULT false,
    email VARCHAR(60) NOT NULL,
    tipoProblemaId INT NOT NULL,
    CONSTRAINT fk_reporteTipoProblema_id FOREIGN KEY (tipoProblemaId) REFERENCES tipoProblema(idTipoProblema) ON DELETE CASCADE
);

-- Creación de la tabla notificacion
CREATE TABLE IF NOT EXISTS notificacion (
	idNotificacion INT AUTO_INCREMENT PRIMARY KEY,
    texto VARCHAR(100) NOT NULL,
    tipo ENUM('Anime', 'Personalizada') NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE,
    diaSemana INT,
    usuarioId INT,
    animeId INT,
    CONSTRAINT fk_notificacionUsuario_id FOREIGN KEY (usuarioId) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_notificacionAnimeId FOREIGN KEY (animeId) REFERENCES anime(idAnime) ON DELETE CASCADE
);

-- Inserciones a la tabla rol
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

-- Inserciones a la tabla usuario
INSERT INTO usuario(nombre, email, password, urlImagen, habilitado, rolId) VALUES
    ('admin', 'admin@gmail.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/superadmin.jpg', 1, 1),
    ('shivita', 'shivita@ejemplogmail1.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/admin.jpg', 1, 1),
    ('saul', 'saul@ejemplogmail2.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/admin.jpg', 1, 1),
    ('auron', 'auronplay@ejemplogmail3.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/avatar6.jpg', 1, 2),
    ('Carola', 'carola@ejemplogmail4.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/avatar4.jpg', 0, 2),
    ('eduardo', 'eledu@ejemplogmail5.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/avatar8.jpg', 1, 2),
    ('isa', 'anaisabelgarmendia@ejemplogmail6.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/avatar7.jpg', 1, 2),
    ('roberto', 'rober@ejemplogmail7.com', '$2a$10$T5NRNFP6ePr3Uwbv0Pn04.kBq8/IUxJ9vvQU9/kvksOosNBB2GBrK', '/Imagenes/FotosPerfil/avatar5.jpg', 0, 2);

-- Inserciones a la tabla genero
INSERT INTO genero (nombre, descripcion) VALUES
	('Shonen', 'Aventuras y Acción'),
    ('Shojo', 'Romances y Relaciones'),
    ('Seinen', 'Temáticas adultas'),
    ('Spokon', 'Deportes'),
    ('Josei', 'Realismo y Vida cotidiana'),
    ('Mecha', 'Robots y Ciencia ficción'),
    ('Isekai', 'Otros mundos'),
    ('Horror', 'Psicológico y Sobrenatural');

-- Inserciones a la tabla estado
INSERT INTO estado (nombre) VALUES ('En emisión'), ('Finalizado'), ('Pausado');

-- Inserciones a la tabla anime
INSERT INTO anime (nombre, descripcion, fechaInicio, fechaFin, diaSemana, urlImagen, generoId, estadoId) VALUES 
('Ao no Hako', 'Taiki Inomata, estudiante de tercer año de secundaria, asiste a la Academia Eimei, una escuela integrada con un importante programa deportivo. Habiendo ingresado en el equipo de bádminton del instituto, Taiki intenta asistir a los entrenamientos abiertos lo antes posible. Pero no importa lo temprano que vaya, siempre llega el segundo al gimnasio. La persona que va un paso por delante de él es Chinatsu Kano, una estudiante de primer año del instituto y que está enamorada de Taiki. Chinatsu es la estrella emergente del equipo de baloncesto, y la diferencia entre ella y Taiki no podría ser mayor. Aunque Taiki es un decente jugador de bádminton, su popularidad no se acerca a la de Chinatsu, lo que hace aún menos probable que sus sentimientos sean recíprocos. Sin embargo, en un extraño giro del destino, ¡acaba viviendo en la casa de Taiki! Deseando convertirse en un digno rival para Chinatsu, Taiki persigue el mismo sueño que su enamorada: participar en los Nacionales. Taiki comienza a entrenar con más ahínco que nunca, todo ello con el fin de establecer una sólida relación con su nueva compañera de casa.', '2025-05-31', NULL, 4, '/Imagenes/Portadas/Ao-no-Hako.jpg', 2, 1),
('Black Clover', 'La historia de Black Clover nos pone en la piel de un joven llamado Asta, el cual quiere convertirse en el mago más grande del reino. Sin embargo, hay un inconveniente: ¡no puede usar magia! Por suerte, Asta recibe el grimorio trébol de cinco hojas, que le otorga el poder de la anti-magia. ¿Puede alguien carente de magia convertirse en el Rey Hechicero? Una cosa está clara, Asta nunca se rendirá.', '2025-05-20', NULL, 5, '/Imagenes/Portadas/Black-Clover.jpg', 1, 1),
('Blue Lock', 'Tras una desastrosa derrota en la Copa Mundial de 2018, el equipo japonés está pasando por un mal momento. ¿Qué es lo que les falta? Un delantero que sea el mejor, alguien que pueda guiarles hasta la victoria. La federación japonesa está decidida a crear un jugador con una sed de gol única, alguien "egoísta" con el balón, un jugador que pueda ser capaz de dar la vuelta a un partido que está por perderse... Y para ello reúnen a 300 de las mejores jóvenes promesas de Japón. ¿Quién resultará el elegido como futuro líder del equipo? ¿Quién será capaz de plantar cara con su fuerza y su ego a cualquiera que se ponga en su camino? ¿Quién erá ese jugador más egoísta que nadie?', '2025-05-31', NULL, 0, '/Imagenes/Portadas/Blue-Lock.jpg', 4, 1),
('Gekai Elise', 'Aoi Takamoto compensa los pecados cometidos en su vida pasada como la malvada princesa Elise y se dedica a salvar a la gente como médica. Sin embargo, un fatídico accidente trunca su expiación y, de repente, regresa a su vida anterior, 10 años antes de su muerte. ¿Su anticipación y su destreza médica la ayudarán a cambiar su pasado y a restaurar un imperio enfermo, o estará el destino condenado a repetirse?', '2025-06-01', NULL, 1, '/Imagenes/Portadas/Gekai-Elise.jpg', 7, 1),
('Horimiya', 'Aunque admirada en la escuela por su amabilidad y destreza académica, la estudiante de preparatoria Kyouko Hori ha estado escondiendo otro lado de ella. Con sus padres a menudo fuera de casa debido al trabajo, Hori tiene que cuidar de su hermano menor y hacer las tareas del hogar, sin tener tiempo para socializar fuera de la escuela. Mientras tanto, Izumi Miyamura es visto como un inquietante otaku que usa anteojos. Sin embargo, en realidad es una persona amable e inepta para estudiar. Además, tiene nueve piercings escondidos detrás de su largo cabello, y un tatuaje a lo largo de su espalda y hombro izquierdo. Por pura casualidad, Hori y Miyamura se cruzan fuera de la escuela, ninguno luciendo como el otro lo esperaría. Estos polos aparentemente opuestos se convierten en amigos, compartiendo un lado que nunca le han mostrado a nadie.', '2021-01-10', '2021-03-14', 4, '/Imagenes/Portadas/Horimiya.jpg', 2, 2),
('Jiisan Baasan Wakagaeru', 'Luego de comer una misteriosa manzana de su granja, Shouzou e Ine, una pareja de ancianos muy enamorados, experimentan una milagrosa transformación y se vuelven jóvenes de nuevo. A pesar de su nueva juventud y aspecto, ambos siguen conservando sus tiernas costumbres de ancianitos. No te pierdas este vínculo tan duradero que ahora será más fuerte y hermoso que nunca.', '2025-05-31', NULL, 3, '/Imagenes/Portadas/Jiisan-Baasan-Wakagaeru.jpg', 5, 1),
('Jujutsu Kaisen', 'Yuuji Itadori es un estudiante de instituto con unas habilidades físicas excepcionales. Todos los días, como rutina, va al hospital a visitar a su abuelo enfermo y decide apuntarse al club de ocultismo del instituto para no dar un palo al agua… Sin embargo, un buen día el sello del talismán que se hallaba escondido en su instituto se rompe, y comienzan a aparecer unos monstruos. Ante este giro de los acontecimientos, Itadori decide adentrarse en el instituto para salvar a sus compañeros. ¿Qué le deparará el destino?', '2025-05-29', NULL, 5, '/Imagenes/Portadas/Jujutsu-Kaisen.jpg', 8, 1),
('Kimetsu no Yaiba', 'Estamos en la era Taisho de Japón. Tanjiro, un joven que se gana la vida vendiendo carbón, descubre un día que su familia ha sido asesinada por un demonio. Para empeorar las cosas, su hermana menor Nezuko, la única superviviente de la masacre, ha sufrido una transformación en demonio. Destrozado por los acontecimientos Tanjiro decide convertirse en un cazador de demonios para poder devolver a su hermana a la normalidad y matar al demonio que masacró a su familia.', '2019-04-06', '2019-06-29', 5, '/Imagenes/Portadas/Kimetsu-no-Yaiba.jpg', 1, 2),
('Kusuriya no Hitorigoto', 'Maomao llevaba una vida tranquila ayudando a su padre, un boticario. Todo cambia el día que la venden como sirvienta al palacio del emperador, pero la vida entre nobles y realeza no es para ella. Cuando la familia imperial enferma, ella decide intervenir para encontrar una cura, lo que llama la atención de Jinshi, un guapo oficial de palacio que decide ascenderla como dama de compañía de una de las concubinas del emperador. ¡Su habilidad con la medicina la hará conocida en el palacio por ayudar a resolver muchos misterios!', '2025-05-30', NULL, 6, '/Imagenes/Portadas/Kusuriya-no-Hitorigoto.jpg', 3, 1),
('Mashle', 'Este es un mundo de magia. Este es un mundo en el que todos usan la magia habitualmente. En un bosque profundo y oscuro de este mundo de magia, hay un chico que se ejercita a diario. Su nombre es Mash Burnedead y tiene un secreto: no puede usar magia. Lo único que quería era vivir tranquilo con su familia, pero cuando un día intentan matarlo por no poder usar magia, las cosas se salen de control y acaba inscrito en una escuela mágica, donde su objetivo será convertirse en el "Iluminado Divino", el alumno más formidable, el elegido de Dios. ¿Podrán sus poderosos músculos derrotar a los más brillantes usuarios de magia? ¡Se alza el telón de esta historia de magia y fantasía peculiar en el que la fuerza puede con la magia!', '2025-05-28', NULL, 6, '/Imagenes/Portadas/Mashle.jpg', 1, 1),
('Nana', 'Dos chicas de la misma edad y mismo nombre se conocieron en el tren para ir a Tokio. Sus nombres son NANA. Una de ellas es una chica que quiere ser famosa con su grupo de música y la otra quiere estar con su novio que vive en Tokio. Después de conocerse ellas deciden vivir juntas en Tokio. Komatsu Nana comienza su nueva vida en Tokio con su novio Shoji, y los amigos de allí, Junko y Kyosuke. Por otro lado, Osaki Nana se prepara para su debut como vocalista de un grupo de punk rock llamado...', '2006-04-05', '2006-08-16', 1, '/Imagenes/Portadas/NANA.jpg', 3, 3),
('Nanatsu no Taizai', 'Los “Siete Pecados Capitales”, un grupo de caballeros malignos que conspiraron para derrocar al Reino de Britania, se dice que fueron erradicados por los Caballeros Sagrados, aunque algunos dicen que aún viven. Diez años después, los Caballeros Sagrados dieron un golpe de estado y asesinaron al rey, convirtiéndose en los nuevos y tiránicos regidores del reino. Elizabeth, la única hija del rey, emprende un viaje para encontrar a los “Siete Pecados Capitales” y conseguir su ayuda para recuperar el reino.', '2014-10-05', '2015-01-11', 2, '/Imagenes/Portadas/Nanatsu-no-Taizai.jpg', 1, 2),
('Ore Dake Level Up Na Ken', 'Lo que no te mata te hace más fuerte, pero en el caso de Sung Jinwoo, lo que lo mató lo hizo más fuerte. Después de ser brutalmente asesinado por monstruos en una mazmorra de alto rango, Jinwoo regresó con el Sistema, un programa que solo él puede ver y que eleva su nivel en todos los sentidos. Ahora, está decidido a descubrir los secretos detrás de sus poderes y la mazmorra que los engendró.', '2023-09-08', '2023-11-24', 5, '/Imagenes/Portadas/Ore-dake-Level-Up-na-Ken.jpg', 1, 2),
('Oshi no ko', 'La chica de 16 años, Ai Hoshino, es una talentosa y hermosa idol que es adorada por sus fanáticos. Ella es la personificación de la pureza, pero no todo lo que brilla es oro… Gorou Honda es un ginecólogo de una zona rural y gran fanático de Ai. Entonces, cuando la idol embarazada se presenta en su hospital, está más que desconcertado. Gorou le promete un parto seguro, pero poco sabe él que un encuentro con una misteriosa figura resultaría en su muerte prematura, o eso pensaba. Al abrir sus ojos, Gorou se encuentra en el regazo de su amada idol y descubre que ha renacido como Aquamarine Hoshino, ¡el hijo recién nacido de Ai! Con su mundo vuelto al revés, Gorou pronto descubre que el mundo del espectáculo está lleno de espinas, y que el talento no siempre genera éxito. ¿Podrá proteger la sonrisa de su amada idol?', '2024-01-05', '2024-03-15', 0, '/Imagenes/Portadas/Oshi-No-Ko.jpg', 3, 2),
('Shingeki no Kyojin', 'La historia nos traslada a un mundo en el que la humanidad estuvo a punto de ser exterminada cientos de años atrás por los gigantes. Los gigantes son enormes, parecen no ser inteligentes y devoran seres humanos. Lo peor es que parece que lo hacen por placer y no por alimentarse. Una pequeña parte de la humanidad ha conseguido sobrevivir protegiéndose en una ciudad con unos altísimos muros, más altos que el mayor de los gigantes. La ciudad vive su vida tranquila, y hace más de 100 años que ningún gigante aparece por allí. Eren y su hermana adoptiva Mikasa son todavía unos adolescentes cuando ven algo horroroso: un gigante mucho mayor que todos los que la humanidad había conocido hasta el momento está destruyendo los muros de la ciudad. No pasa mucho tiempo hasta que los gigantes entran por el hueco abierto en el muro y comienzan a devorar a la gente.', '2013-04-07', '2013-06-23', 4, '/Imagenes/Portadas/Shingeki-no-Kyojin.jpg', 1, 2),
('Sousou no Frieren', 'La maga Frieren formaba parte del grupo del héroe Himmel, quienes derrotaron al Rey Demonio tras un viaje de 10 años y devolvieron la paz al reino. Frieren es una elfa de más de mil años de vida, así que al despedirse de Himmel y sus compañeros promete que regresará para verlos y parte de viaje sola. Al cabo de cincuenta años, Frieren cumple su promesa y acude a visitar a Himmel y al resto. Aunque ella no ha cambiado, Himmel y los demás han envejecido y están en el final de sus vidas. Cuando Himmel muere, Frieren se arrepiente de no haber pasado más tiempo a su lado conociéndolo, así que emprende un viaje para conocer mejor a sus antiguos compañeros, a las personas y descubrir más del mundo.', '2025-05-23', NULL, 6, '/Imagenes/Portadas/Sousou-no-Frieren.jpg', 7, 1),
('Tokyo Ghoul', 'Extraños asesinatos se están sucediendo uno tras otro en Tokyo. Debido a las pruebas encontradas en las escenas, la policía concluye que los ataques son obra de ghouls que se comen a las personas. Kaneki y Hide, dos compañeros de clase, llegan a la conclusión de que si nadie ha visto nunca a esos necrófagos es porque toman la apariencia de seres humanos para ocultarse. Poco sabían entonces de que su teoría sería más cierta de lo que pensaban cuando Kaneki es herido de gravedad por un monstruo y comienza a atraerle cada vez más la carne humana…', '2014-07-04', '2014-09-19', 3, '/Imagenes/Portadas/Tokyo-Ghoul.jpg', 8, 2),
('Tokyo Revengers', 'Mientras miraba las noticias, Takemichi Hanagaki se entera de que su novia de secundaria, Hinata Tachibana, ha muerto. La única chica que alguna vez se fijó en él fue asesinada por un grupo de criminales conocidos como la Banda Tokyo Manji. Takemichi vive en un viejo departamento con delgadas paredes, y en su trabajo, su jefa seis años menor que él lo trata como basura. Para rematar, es un completo virgen… En la cúspide de la miseria de su vida, de repente vuelve en el tiempo doce años a sus días de secundaria. Para salvar a Hinata y cambiar el curso del tiempo, ¡el alguna vez inútil trabajador de medio tiempo Takemichi deberá buscar volverse el líder de la banda criminal más tenebrosa de Tokio!', '2021-04-11', '2021-07-04', 1, '/Imagenes/Portadas/Tokyo-Revengers.jpg', 1, 2),
('Wind Breaker', 'El lugar donde las calificaciones medias son las peores, pero las peleas son las mejores. La Preparatoria Furin es bien conocida por ser una preparatoria repleta de delincuentes. Haruka Sakura, estudiante de primer año, llega como novato con ganas de abrirse paso luchando hasta lo más alto. Sin embargo, la Preparatoria Furin tiene ahora un grupo que protege las calles de la ciudad con el nombre de “Wind Breaker”. ¡Este es el inicio de la leyenda de Sakura, el delincuente de preparatoria!', '2024-10-15', '2024-12-24', 4, '/Imagenes/Portadas/Wind-Breaker.jpg', 1, 2),
('Yubisaki to Renren', 'Yuki Itose es una típica estudiante que lidia con las presiones de la universidad. Un día tiene problemas en el tren cuando un compañero llamado Itsuomi Nagi le echa una mano. A medida que él el abre un mundo nuevo a ella, Yuki empieza a sentir algo por Itsuomi. Una historia de amor pura empieza a crecer.', '2023-11-25', '2024-02-10', 5, '/Imagenes/Portadas/Yubisaki-to-Renren.jpg', 2, 2);

-- Inserciones en la tabla episodio por anime
INSERT INTO episodio (animeId, numEpisodio, urlVideo, fechaLanzamiento, urlPoster) VALUES
(1, 1, '/Videos/Ao-no-Hako/4057_1.mp4', '2025-05-31', '/Imagenes/Poster/Ao-no-Hako.jpg'),
(1, 2, '/Videos/Ao-no-Hako/4057_2.mp4', '2025-06-07', '/Imagenes/Poster/Ao-no-Hako.jpg'),
(1, 3, '/Videos/Ao-no-Hako/4057_3.mp4', '2025-06-14', '/Imagenes/Poster/Ao-no-Hako.jpg'),
(2, 1, '/Videos/Black-Clover/3910_1.mp4', '2025-05-20', '/Imagenes/Poster/Black-Clover.jpg'),
(2, 2, '/Videos/Black-Clover/3910_2.mp4', '2025-05-27', '/Imagenes/Poster/Black-Clover.jpg'),
(2, 3, '/Videos/Black-Clover/3910_3.mp4', '2025-06-03', '/Imagenes/Poster/Black-Clover.jpg'),
(2, 4, '/Videos/Black-Clover/3910_4.mp4', '2025-06-10', '/Imagenes/Poster/Black-Clover.jpg'),
(3, 1, '/Videos/Blue-Lock/3756_1.mp4', '2025-05-31', '/Imagenes/Poster/Blue-Lock.jpg'),
(3, 2, '/Videos/Blue-Lock/3756_2.mp4', '2025-06-07', '/Imagenes/Poster/Blue-Lock.jpg'),
(3, 3, '/Videos/Blue-Lock/3756_3.mp4', '2025-06-14', '/Imagenes/Poster/Blue-Lock.jpg'),
(4, 1, '/Videos/Gekai-Elise/3940_1.mp4', '2025-06-01', '/Imagenes/Poster/Gekai-Elise.jpg'),
(4, 2, '/Videos/Gekai-Elise/3940_2.mp4', '2025-06-08', '/Imagenes/Poster/Gekai-Elise.jpg'),
(4, 3, '/Videos/Gekai-Elise/3940_3.mp4', '2025-06-15', '/Imagenes/Poster/Gekai-Elise.jpg'),
(5, 1, '/Videos/Horimiya/3498_1.mp4', '2021-01-10', '/Imagenes/Poster/Horimiya.jpg'),
(5, 2, '/Videos/Horimiya/3498_2.mp4', '2021-01-17', '/Imagenes/Poster/Horimiya.jpg'),
(5, 3, '/Videos/Horimiya/3498_3.mp4', '2021-01-24', '/Imagenes/Poster/Horimiya.jpg'),
(5, 4, '/Videos/Horimiya/3498_4.mp4', '2021-01-31', '/Imagenes/Poster/Horimiya.jpg'),
(5, 5, '/Videos/Horimiya/3498_5.mp4', '2021-02-07', '/Imagenes/Poster/Horimiya.jpg'),
(5, 6, '/Videos/Horimiya/3498_6.mp4', '2021-02-14', '/Imagenes/Poster/Horimiya.jpg'),
(5, 7, '/Videos/Horimiya/3498_7.mp4', '2021-02-21', '/Imagenes/Poster/Horimiya.jpg'),
(5, 8, '/Videos/Horimiya/3498_8.mp4', '2021-02-28', '/Imagenes/Poster/Horimiya.jpg'),
(5, 9, '/Videos/Horimiya/3498_9.mp4', '2021-03-07', '/Imagenes/Poster/Horimiya.jpg'),
(5, 10, '/Videos/Horimiya/3498_10.mp4', '2021-03-14', '/Imagenes/Poster/Horimiya.jpg'),
(6, 1, '/Videos/Jiisan-Baasan-Wakagaeru/3977_1.mp4', '2025-05-31', '/Imagenes/Poster/Jiisan-Baasan-Wakagaeru.jpg'),
(6, 2, '/Videos/Jiisan-Baasan-Wakagaeru/3977_2.mp4', '2025-06-07', '/Imagenes/Poster/Jiisan-Baasan-Wakagaeru.jpg'),
(6, 3, '/Videos/Jiisan-Baasan-Wakagaeru/3977_3.mp4', '2025-06-14', '/Imagenes/Poster/Jiisan-Baasan-Wakagaeru.jpg'),
(7, 1, '/Videos/Jujutsu-Kaisen/3101_1.mp4', '2025-05-29', '/Imagenes/Poster/Jujutsu-Kaisen.jpg'),
(7, 2, '/Videos/Jujutsu-Kaisen/3101_2.mp4', '2025-06-05', '/Imagenes/Poster/Jujutsu-Kaisen.jpg'),
(7, 3, '/Videos/Jujutsu-Kaisen/3101_3.mp4', '2025-06-12', '/Imagenes/Poster/Jujutsu-Kaisen.jpg'),
(8, 1, '/Videos/Kimetsu-no-Yaiba/3945_1.mp4', '2019-04-06', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 2, '/Videos/Kimetsu-no-Yaiba/3945_2.mp4', '2019-04-13', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 3, '/Videos/Kimetsu-no-Yaiba/3945_3.mp4', '2019-04-20', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 4, '/Videos/Kimetsu-no-Yaiba/3945_4.mp4', '2019-04-27', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 5, '/Videos/Kimetsu-no-Yaiba/3945_5.mp4', '2019-05-04', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 6, '/Videos/Kimetsu-no-Yaiba/3945_6.mp4', '2019-05-11', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 7, '/Videos/Kimetsu-no-Yaiba/3945_7.mp4', '2019-05-18', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 8, '/Videos/Kimetsu-no-Yaiba/3945_8.mp4', '2019-05-25', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 9, '/Videos/Kimetsu-no-Yaiba/3945_9.mp4', '2019-06-01', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 10, '/Videos/Kimetsu-no-Yaiba/3945_10.mp4', '2019-06-08', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 11, '/Videos/Kimetsu-no-Yaiba/3945_11.mp4', '2019-06-15', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 12, '/Videos/Kimetsu-no-Yaiba/3945_12.mp4', '2019-06-22', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(8, 13, '/Videos/Kimetsu-no-Yaiba/3945_13.mp4', '2019-06-29', '/Imagenes/Poster/Kimetsu-no-Yaiba.jpg'),
(9, 1, '/Videos/Kusuriya-no-Hitorigoto/3909_1.mp4', '2025-05-30', '/Imagenes/Poster/Kusuriya-no-Hitorigoto.jpg'),
(9, 2, '/Videos/Kusuriya-no-Hitorigoto/3909_2.mp4', '2025-06-06', '/Imagenes/Poster/Kusuriya-no-Hitorigoto.jpg'),
(9, 3, '/Videos/Kusuriya-no-Hitorigoto/3909_3.mp4', '2025-06-13', '/Imagenes/Poster/Kusuriya-no-Hitorigoto.jpg'),
(10, 1, '/Videos/Mashle/3678_1.mp4', '2025-05-28', '/Imagenes/Poster/Mashle.jpg'),
(10, 2, '/Videos/Mashle/3678_2.mp4', '2025-06-04', '/Imagenes/Poster/Mashle.jpg'),
(10, 3, '/Videos/Mashle/3678_3.mp4', '2025-06-11', '/Imagenes/Poster/Mashle.jpg'),
(11, 1, '/Videos/Nana/3636_1.mp4', '2006-04-05', '/Imagenes/Poster/NANA.jpg'),
(11, 2, '/Videos/Nana/3636_2.mp4', '2006-04-12', '/Imagenes/Poster/NANA.jpg'),
(11, 3, '/Videos/Nana/3636_3.mp4', '2006-04-19', '/Imagenes/Poster/NANA.jpg'),
(11, 4, '/Videos/Nana/3636_4.mp4', '2006-04-26', '/Imagenes/Poster/NANA.jpg'),
(11, 5, '/Videos/Nana/3636_5.mp4', '2006-05-03', '/Imagenes/Poster/NANA.jpg'),
(11, 6, '/Videos/Nana/3636_6.mp4', '2006-05-10', '/Imagenes/Poster/NANA.jpg'),
(11, 7, '/Videos/Nana/3636_7.mp4', '2006-05-17', '/Imagenes/Poster/NANA.jpg'),
(11, 8, '/Videos/Nana/3636_8.mp4', '2006-05-24', '/Imagenes/Poster/NANA.jpg'),
(11, 9, '/Videos/Nana/3636_9.mp4', '2006-05-31', '/Imagenes/Poster/NANA.jpg'),
(11, 10, '/Videos/Nana/3636_10.mp4', '2006-06-07', '/Imagenes/Poster/NANA.jpg'),
(11, 11, '/Videos/Nana/3636_11.mp4', '2006-06-14', '/Imagenes/Poster/NANA.jpg'),
(11, 12, '/Videos/Nana/3636_12.mp4', '2006-06-21', '/Imagenes/Poster/NANA.jpg'),
(11, 13, '/Videos/Nana/3636_13.mp4', '2006-06-28', '/Imagenes/Poster/NANA.jpg'),
(11, 14, '/Videos/Nana/3636_14.mp4', '2006-07-05', '/Imagenes/Poster/NANA.jpg'),
(11, 15, '/Videos/Nana/3636_15.mp4', '2006-07-12', '/Imagenes/Poster/NANA.jpg'),
(11, 16, '/Videos/Nana/3636_16.mp4', '2006-07-19', '/Imagenes/Poster/NANA.jpg'),
(11, 17, '/Videos/Nana/3636_17.mp4', '2006-07-26', '/Imagenes/Poster/NANA.jpg'),
(11, 18, '/Videos/Nana/3636_18.mp4', '2006-08-02', '/Imagenes/Poster/NANA.jpg'),
(11, 19, '/Videos/Nana/3636_19.mp4', '2006-08-09', '/Imagenes/Poster/NANA.jpg'),
(11, 20, '/Videos/Nana/3636_20.mp4', '2006-08-16', '/Imagenes/Poster/NANA.jpg'),
(12, 1, '/Videos/Nanatsu-no-Taizai/3521_1.mp4', '2014-10-05', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 2, '/Videos/Nanatsu-no-Taizai/3521_2.mp4', '2014-10-12', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 3, '/Videos/Nanatsu-no-Taizai/3521_3.mp4', '2014-10-19', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 4, '/Videos/Nanatsu-no-Taizai/3521_4.mp4', '2014-10-26', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 5, '/Videos/Nanatsu-no-Taizai/3521_5.mp4', '2014-11-02', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 6, '/Videos/Nanatsu-no-Taizai/3521_6.mp4', '2014-11-09', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 7, '/Videos/Nanatsu-no-Taizai/3521_7.mp4', '2014-11-16', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 8, '/Videos/Nanatsu-no-Taizai/3521_8.mp4', '2014-11-23', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 9, '/Videos/Nanatsu-no-Taizai/3521_9.mp4', '2014-11-30', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 10, '/Videos/Nanatsu-no-Taizai/3521_10.mp4', '2014-12-07', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 11, '/Videos/Nanatsu-no-Taizai/3521_11.mp4', '2014-12-14', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 12, '/Videos/Nanatsu-no-Taizai/3521_12.mp4', '2014-12-21', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 13, '/Videos/Nanatsu-no-Taizai/3521_13.mp4', '2014-12-28', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 14, '/Videos/Nanatsu-no-Taizai/3521_14.mp4', '2015-01-04', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(12, 15, '/Videos/Nanatsu-no-Taizai/3521_15.mp4', '2015-01-11', '/Imagenes/Poster/Nanatsu-no-Taizai.jpg'),
(13, 1, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_1.mp4', '2023-09-08', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 2, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_2.mp4', '2023-09-15', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 3, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_3.mp4', '2023-09-22', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 4, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_4.mp4', '2023-09-29', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 5, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_5.mp4', '2023-10-06', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 6, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_6.mp4', '2023-10-13', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 7, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_7.mp4', '2023-10-20', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 8, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_8.mp4', '2023-10-27', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 9, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_9.mp4', '2023-11-03', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 10, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_10.mp4', '2023-11-10', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 11, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_11.mp4', '2023-11-17', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(13, 12, '/Videos/Ore-Dake-Level-Up-Na-Ken/3928_12.mp4', '2023-11-24', '/Imagenes/Poster/Ore-dake-Level-Up-na-Ken.jpg'),
(14, 1, '/Videos/Oshi-no-ko/3963_1.mp4', '2024-01-05', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 2, '/Videos/Oshi-no-ko/3963_2.mp4', '2024-01-12', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 3, '/Videos/Oshi-no-ko/3963_3.mp4', '2024-01-19', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 4, '/Videos/Oshi-no-ko/3963_4.mp4', '2024-01-26', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 5, '/Videos/Oshi-no-ko/3963_5.mp4', '2024-02-02', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 6, '/Videos/Oshi-no-ko/3963_6.mp4', '2024-02-09', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 7, '/Videos/Oshi-no-ko/3963_7.mp4', '2024-02-16', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 8, '/Videos/Oshi-no-ko/3963_8.mp4', '2024-02-23', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 9, '/Videos/Oshi-no-ko/3963_9.mp4', '2024-03-01', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(14, 10, '/Videos/Oshi-no-ko/3963_10.mp4', '2024-03-08', '/Imagenes/Poster/Oshi-No-Ko.jpg'),
(15, 1, '/Videos/Shingeki-no-Kyojin/3441_1.mp4', '2013-04-07', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 2, '/Videos/Shingeki-no-Kyojin/3441_2.mp4', '2013-04-14', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 3, '/Videos/Shingeki-no-Kyojin/3441_3.mp4', '2013-04-21', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 4, '/Videos/Shingeki-no-Kyojin/3441_4.mp4', '2013-04-28', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 5, '/Videos/Shingeki-no-Kyojin/3441_5.mp4', '2013-05-05', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 6, '/Videos/Shingeki-no-Kyojin/3441_6.mp4', '2013-05-12', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 7, '/Videos/Shingeki-no-Kyojin/3441_7.mp4', '2013-05-19', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 8, '/Videos/Shingeki-no-Kyojin/3441_8.mp4', '2013-05-26', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 9, '/Videos/Shingeki-no-Kyojin/3441_9.mp4', '2013-06-02', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 10, '/Videos/Shingeki-no-Kyojin/3441_10.mp4', '2013-06-09', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 11, '/Videos/Shingeki-no-Kyojin/3441_11.mp4', '2013-06-16', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(15, 12, '/Videos/Shingeki-no-Kyojin/3441_12.mp4', '2013-06-23', '/Imagenes/Poster/Shingeki-no-Kyojin.jpg'),
(16, 1, '/Videos/Sousou-no-Frieren/3859_1.mp4', '2025-05-23', '/Imagenes/Poster/Sousou-no-Frieren.jpg'),
(16, 2, '/Videos/Sousou-no-Frieren/3859_2.mp4', '2025-05-30', '/Imagenes/Poster/Sousou-no-Frieren.jpg'),
(16, 3, '/Videos/Sousou-no-Frieren/3859_3.mp4', '2025-06-06', '/Imagenes/Poster/Sousou-no-Frieren.jpg'),
(16, 4, '/Videos/Sousou-no-Frieren/3859_4.mp4', '2025-06-13', '/Imagenes/Poster/Sousou-no-Frieren.jpg'),
(17, 1, '/Videos/Tokyo-Ghoul/3325_1.mp4', '2014-07-04', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 2, '/Videos/Tokyo-Ghoul/3325_2.mp4', '2014-07-11', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 3, '/Videos/Tokyo-Ghoul/3325_3.mp4', '2014-07-18', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 4, '/Videos/Tokyo-Ghoul/3325_4.mp4', '2014-07-25', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 5, '/Videos/Tokyo-Ghoul/3325_5.mp4', '2014-08-01', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 6, '/Videos/Tokyo-Ghoul/3325_6.mp4', '2014-08-08', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 7, '/Videos/Tokyo-Ghoul/3325_7.mp4', '2014-08-15', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 8, '/Videos/Tokyo-Ghoul/3325_8.mp4', '2014-08-22', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 9, '/Videos/Tokyo-Ghoul/3325_9.mp4', '2014-08-29', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 10, '/Videos/Tokyo-Ghoul/3325_10.mp4', '2014-09-05', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 11, '/Videos/Tokyo-Ghoul/3325_11.mp4', '2014-09-12', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(17, 12, '/Videos/Tokyo-Ghoul/3325_12.mp4', '2014-09-19', '/Imagenes/Poster/Tokyo-Ghoul.jpg'),
(18, 1, '/Videos/Tokyo-Revengers/4129_1.mp4', '2021-04-11', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 2, '/Videos/Tokyo-Revengers/4129_2.mp4', '2021-04-18', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 3, '/Videos/Tokyo-Revengers/4129_3.mp4', '2021-04-25', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 4, '/Videos/Tokyo-Revengers/4129_4.mp4', '2021-05-02', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 5, '/Videos/Tokyo-Revengers/4129_5.mp4', '2021-05-09', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 6, '/Videos/Tokyo-Revengers/4129_6.mp4', '2021-05-16', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 7, '/Videos/Tokyo-Revengers/4129_7.mp4', '2021-05-23', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 8, '/Videos/Tokyo-Revengers/4129_8.mp4', '2021-05-30', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 9, '/Videos/Tokyo-Revengers/4129_9.mp4', '2021-06-06', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 10, '/Videos/Tokyo-Revengers/4129_10.mp4', '2021-06-13', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 11, '/Videos/Tokyo-Revengers/4129_11.mp4', '2021-06-20', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 12, '/Videos/Tokyo-Revengers/4129_12.mp4', '2021-06-27', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(18, 13, '/Videos/Tokyo-Revengers/4129_13.mp4', '2021-07-04', '/Imagenes/Poster/Tokyo-Revengers.jpg'),
(19, 1, '/Videos/Wind-Breaker/3988_1.mp4', '2024-10-15', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 2, '/Videos/Wind-Breaker/3988_2.mp4', '2024-10-22', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 3, '/Videos/Wind-Breaker/3988_3.mp4', '2024-10-29', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 4, '/Videos/Wind-Breaker/3988_4.mp4', '2024-11-05', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 5, '/Videos/Wind-Breaker/3988_5.mp4', '2024-11-12', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 6, '/Videos/Wind-Breaker/3988_6.mp4', '2024-11-19', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 7, '/Videos/Wind-Breaker/3988_7.mp4', '2024-11-26', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 8, '/Videos/Wind-Breaker/3988_8.mp4', '2024-12-03', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 9, '/Videos/Wind-Breaker/3988_9.mp4', '2024-12-10', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 10, '/Videos/Wind-Breaker/3988_10.mp4', '2024-12-17', '/Imagenes/Poster/Wind-Breaker.jpg'),
(19, 11, '/Videos/Wind-Breaker/3988_11.mp4', '2024-12-24', '/Imagenes/Poster/Wind-Breaker.jpg'),
(20, 1, '/Videos/Yubisaki-to-Renren/3927_1.mp4', '2023-11-25', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 2, '/Videos/Yubisaki-to-Renren/3927_2.mp4', '2023-12-02', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 3, '/Videos/Yubisaki-to-Renren/3927_3.mp4', '2023-12-09', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 4, '/Videos/Yubisaki-to-Renren/3927_4.mp4', '2023-12-16', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 5, '/Videos/Yubisaki-to-Renren/3927_5.mp4', '2023-12-23', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 6, '/Videos/Yubisaki-to-Renren/3927_6.mp4', '2023-12-30', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 7, '/Videos/Yubisaki-to-Renren/3927_7.mp4', '2024-01-06', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 8, '/Videos/Yubisaki-to-Renren/3927_8.mp4', '2024-01-13', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 9, '/Videos/Yubisaki-to-Renren/3927_9.mp4', '2024-01-20', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 10, '/Videos/Yubisaki-to-Renren/3927_10.mp4', '2024-01-27', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 11, '/Videos/Yubisaki-to-Renren/3927_11.mp4', '2024-02-03', '/Imagenes/Poster/Yubisaki-to-Renren.jpg'),
(20, 12, '/Videos/Yubisaki-to-Renren/3927_12.mp4', '2024-02-10', '/Imagenes/Poster/Yubisaki-to-Renren.jpg');

-- Inserciones a la tabla tipoProblema
INSERT INTO tipoProblema (nombre) VALUES 
	('Inicio'),
	('Directorio'),
	('Calendario'),
	('Página principal de un anime'),
    ('Página de visualización de un episodio'),
    ('Búsqueda de un anime'),
    ('Inicio de sesión'),
    ('Otro (especificar)');

-- Inserciones a la tabla comentarEpisodio
INSERT INTO comentarEpisodio(usuarioId, animeId, numEpisodio, habilitado, comentario, fechaCreacion) VALUES
(4, 1, 3, 1, 'Esto se está poniendo bueno.', '2025-06-05 10:18:07'),
(2, 1, 1, 1, '¡Estoy enganchado con esta serie!', '2025-06-02 00:30:37'),
(3, 1, 2, 1, 'El protagonista por fin hizo algo épico.', '2025-06-05 01:54:54'),
(7, 2, 4, 1, 'El protagonista por fin hizo algo épico.', '2025-06-15 03:30:11'),
(2, 1, 2, 1, 'Me reí más de lo que debería.', '2025-06-07 17:41:28'),
(6, 1, 3, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-06 01:50:52'),
(1, 1, 2, 1, '¡Me hizo llorar! Qué final tan emotivo.', '2025-06-12 01:51:38'),
(1, 2, 3, 1, '¡Me hizo llorar! Qué final tan emotivo.', '2025-06-15 15:33:31'),
(6, 3, 3, 1, 'El antagonista me cae cada vez peor.', '2025-06-03 12:41:53'),
(1, 3, 3, 1, 'La animación estuvo increíble, no me lo esperaba.', '2025-06-01 14:54:45'),
(3, 3, 2, 1, 'El ritmo fue perfecto.', '2025-06-09 02:16:27'),
(7, 3, 2, 1, 'Gran cierre para este arco.', '2025-06-12 06:45:35'),
(6, 3, 2, 0, 'Ni los fans más ciegos pueden defender esto.', '2025-06-11 13:03:37'),
(2, 4, 1, 0, 'Este anime apesta.', '2025-06-15 06:59:29'),
(4, 4, 1, 0, 'El estudio debería disculparse.', '2025-06-10 07:19:00'),
(1, 4, 3, 1, 'No puedo esperar al próximo episodio.', '2025-06-13 00:23:30'),
(2, 4, 2, 1, '¡Estoy enganchado con esta serie!', '2025-06-04 15:47:10'),
(6, 5, 1, 0, 'No sé cómo hay gente que ve esto.', '2025-06-06 12:50:34'),
(1, 5, 8, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-15 15:22:35'),
(3, 5, 8, 0, 'El estudio debería disculparse.', '2025-06-14 23:48:13'),
(7, 5, 10, 1, '¡Qué capítulo tan redondo!', '2025-06-11 10:49:30'),
(7, 6, 1, 1, 'Increíble el desarrollo de la historia.', '2025-06-04 06:00:37'),
(6, 6, 2, 1, 'Gran cierre para este arco.', '2025-06-15 17:57:32'),
(2, 6, 3, 1, 'Muy lento este episodio, pero se entiende por la trama.', '2025-06-04 09:17:42'),
(4, 6, 3, 1, 'El ritmo fue perfecto.', '2025-06-07 16:07:25'),
(6, 6, 3, 1, 'Música, animación, historia... todo 10/10.', '2025-06-05 04:52:16'),
(6, 7, 3, 1, 'El ritmo fue perfecto.', '2025-06-08 03:50:25'),
(4, 7, 2, 1, 'Increíble el desarrollo de la historia.', '2025-06-14 05:04:35'),
(4, 8, 1, 1, 'Música, animación, historia... todo 10/10.', '2025-06-05 01:19:46'),
(6, 8, 6, 1, 'El ritmo fue perfecto.', '2025-06-15 10:17:33'),
(2, 8, 11, 0, 'Qué basura de episodio.', '2025-06-04 23:08:37'),
(2, 8, 13, 1, 'Música, animación, historia... todo 10/10.', '2025-06-01 05:49:46'),
(1, 8, 13, 1, 'El antagonista me cae cada vez peor.', '2025-06-02 16:52:53'),
(4, 8, 13, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-10 06:25:25'),
(7, 9, 1, 0, 'Vaya porquería.', '2025-06-07 18:05:32'),
(1, 9, 2, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-06 12:09:37'),
(2, 9, 3, 0, 'Vaya porquería.', '2025-06-08 20:48:06'),
(4, 9, 3, 1, 'Música, animación, historia... todo 10/10.', '2025-06-14 20:51:56'),
(6, 9, 3, 0, 'Este anime apesta.', '2025-06-06 08:24:29'),
(7, 9, 3, 0, 'Insufrible, aburrido y sin sentido.', '2025-06-06 12:31:49'),
(1, 10, 1, 1, 'El ritmo fue perfecto.', '2025-06-15 03:09:13'),
(7, 10, 1, 0, 'Este anime apesta.', '2025-06-08 21:32:08'),
(6, 10, 1, 0, 'No sé cómo hay gente que ve esto.', '2025-06-07 08:32:00'),
(6, 10, 2, 1, 'No puedo esperar al próximo episodio.', '2025-06-11 08:08:44'),
(4, 10, 2, 1, 'Música, animación, historia... todo 10/10.', '2025-06-15 09:31:39'),
(2, 10, 3, 0, 'Todo mal, una pérdida de tiempo.', '2025-06-05 22:36:15'),
(3, 11, 5, 0, 'Insufrible, aburrido y sin sentido.', '2025-06-10 06:22:03'),
(7, 11, 11, 0, 'Este anime apesta.', '2025-06-06 14:10:16'),
(4, 11, 13, 1, '¡Me hizo llorar! Qué final tan emotivo.', '2025-06-05 19:36:48'),
(6, 11, 14, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-03 22:40:19'),
(1, 11, 19, 1, 'Los personajes secundarios se robaron el episodio.', '2025-06-11 05:33:36'),
(7, 11, 20, 1, 'Esto se está poniendo bueno.', '2025-06-05 05:03:22'),
(4, 12, 2, 1, '¡Estoy enganchado con esta serie!', '2025-06-14 12:27:18'),
(1, 12, 4, 1, 'Demasiado relleno para mi gusto.', '2025-06-01 03:38:13'),
(2, 12, 5, 1, '¡Me hizo llorar! Qué final tan emotivo.', '2025-06-13 14:33:49'),
(6, 12, 9, 0, 'Todo mal, una pérdida de tiempo.', '2025-06-04 21:23:41'),
(2, 12, 11, 1, 'Demasiado relleno para mi gusto.', '2025-06-10 19:37:13'),
(3, 13, 11, 1, 'El protagonista por fin hizo algo épico.', '2025-06-12 07:41:15'),
(6, 14, 1, 1, 'El ritmo fue perfecto.', '2025-06-03 17:22:19'),
(1, 14, 2, 1, '¡Ese giro fue una locura!', '2025-06-09 03:26:22'),
(2, 14, 9, 1, 'No puedo esperar al próximo episodio.', '2025-06-01 11:25:42'),
(1, 14, 9, 1, '¡Qué capítulo tan redondo!', '2025-06-15 02:39:29'),
(7, 14, 10, 1, 'Me reí más de lo que debería.', '2025-06-04 09:02:17'),
(6, 15, 4, 1, 'Demasiado relleno para mi gusto.', '2025-06-13 16:37:11'),
(4, 15, 6, 1, 'El ritmo fue perfecto.', '2025-06-11 23:16:18'),
(1, 15, 11, 0, 'Este anime apesta.', '2025-06-04 13:02:51'),
(7, 15, 12, 0, 'Qué basura de episodio.', '2025-06-08 12:19:48'),
(3, 16, 1, 1, '¡Ese giro fue una locura!', '2025-06-10 08:06:40'),
(2, 16, 2, 1, 'No puedo esperar al próximo episodio.', '2025-06-07 09:39:05'),
(4, 16, 3, 1, 'No puedo esperar al próximo episodio.', '2025-06-09 13:11:08'),
(2, 16, 4, 1, 'No puedo esperar al próximo episodio.', '2025-06-08 23:53:46'),
(1, 17, 9, 1, 'Algo flojo, pero aún así disfrutable.', '2025-06-08 13:04:06'),
(4, 17, 10, 1, 'Esto se está poniendo bueno.', '2025-06-08 04:46:31'),
(6, 17, 11, 1, 'Esto se está poniendo bueno.', '2025-06-04 17:28:11'),
(4, 17, 11, 1, 'La animación estuvo increíble, no me lo esperaba.', '2025-06-08 11:18:53'),
(7, 17, 12, 1, 'Me reí más de lo que debería.', '2025-06-04 02:28:44'),
(1, 17, 12, 0, 'Todo mal, una pérdida de tiempo.', '2025-06-15 04:26:13'),
(3, 17, 12, 1, 'Los personajes secundarios se robaron el episodio.', '2025-06-10 05:23:12'),
(2, 17, 12, 1, 'El protagonista por fin hizo algo épico.', '2025-06-12 13:26:45'),
(2, 18, 2, 1, 'Los personajes secundarios se robaron el episodio.', '2025-06-02 15:51:20'),
(1, 18, 13, 1, 'Música, animación, historia... todo 10/10.', '2025-06-11 08:48:52'),
(2, 18, 13, 1, 'Me reí más de lo que debería.', '2025-06-08 22:32:36'),
(3, 18, 13, 1, 'Demasiado relleno para mi gusto.', '2025-06-14 01:22:27'),
(3, 19, 1, 1, 'Increíble el desarrollo de la historia.', '2025-06-12 08:24:18'),
(4, 19, 7, 0, 'Vaya porquería.', '2025-06-12 19:45:05'),
(1, 19, 9, 1, 'Esto se está poniendo bueno.', '2025-06-07 03:42:10'),
(4, 19, 10, 1, 'Esto se está poniendo bueno.', '2025-06-13 20:16:18'),
(2, 19, 11, 1, 'El ritmo fue perfecto.', '2025-06-07 12:08:09'),
(1, 19, 11, 1, 'No sé qué pensar, pero fue intenso.', '2025-06-08 00:50:50'),
(1, 19, 11, 1, 'Música, animación, historia... todo 10/10.', '2025-06-02 10:00:38');

-- Inserciones a la tabla favoritoAnimeUsuario
INSERT INTO favoritoAnimeUsuario (usuarioId, animeId, habilitado) VALUES 
    (1,1,1),
    (1,2,1),
    (1,3,1),
	(2,4,1),
	(2,5,1),
	(2,6,1),
	(3,7,1),
	(3,8,1),
	(3,9,1),
	(4,10,1),
	(4,11,1),
	(4,12,1),
	(5,13,1),
	(5,14,1),
	(5,15,1),
	(6,16,1),
	(6,17,1),
	(6,18,1);

-- Inserciones a la tabla notificacion - personalizadas
INSERT INTO notificacion (texto, tipo, fechaInicio, fechaFin, diaSemana, usuarioId) VALUES 
	('Modificar descripcion','Personalizada','2025-07-15','2025-07-07',0,1),
	('Cambiar episodios 3 y 4','Personalizada','2025-07-15','2025-07-08',1,1),
    ('Añadir nuevo anime','Personalizada','2025-07-09','2025-07-09',2,1),
    ('Revisar reportes','Personalizada','2025-07-11','2025-07-11',4,1),
    ('Revisar usuarios','Personalizada','2025-07-17','2025-07-17',3,1),
	('Este finde no hay capitulo','Personalizada','2025-07-04','2025-07-04',4,2),
	('Este finde no hay capitulo','Personalizada','2025-07-11','2025-07-11',4,2),
	('Este finde no hay capitulo','Personalizada','2025-07-18','2025-07-18',4,2),
	('Comprar Manga nuevo','Personalizada','2025-07-15','2025-07-15',1,2),
	('Pelicula nueva','Personalizada','2025-07-11','2025-07-11',4,2);

-- Inserciones a la tabla notificacion - animes favoritos
INSERT INTO notificacion (texto, tipo, fechaInicio, diaSemana, usuarioId, animeId) VALUES 
	('Nuevo capitulo de Ao no Hako', 'Anime', '2025-05-31', 4, 1, 1),
    ('Nuevo capitulo de Black Clover', 'Anime', '2025-05-20', 5, 1, 2),
    ('Nuevo capitulo de Blue Lock', 'Anime', '2025-05-31', 0, 1, 3),
    ('Nuevo capitulo de Gekai Elise', 'Anime', '2025-06-01', 1, 2, 4),
    ('Nuevo capitulo de Jiisan Baasan Wakagaeru', 'Anime', '2025-05-31', 3, 2, 6),
	('Nuevo capitulo de Jujutsu Kaisen', 'Anime', '2025-05-29', 5, 3, 7),
	('Nuevo capitulo de Kusuriya no Hitorigoto', 'Anime', '2025-05-30', 6, 3, 9),
    ('Nuevo capitulo de Mashle', 'Anime', '2025-05-28', 6, 4, 10),
	('Nuevo capitulo de Sousou no Frieren', 'Anime', '2025-05-23', 6, 6, 16);

-- Inserciones a la tabla reporte
INSERT INTO reporte (descripcion, corregido, email, tipoProblemaId) VALUES 
	('Hay un error en la descripcion de Ao no Hako.', 1, 'anaisabelgarmendia@ejemplogmail6.com', 3),
	('Los capitulos 3 y 4 de black clover estan intercambiados.', 1, 'anaisabelgarmendia@ejemplogmail6.com', 3),
    ('No he podido añadir una fecha al calendario', 1, 'auronplay@ejemplogmail3.com', 2),
    ('No he recibido el correo para cambiar la contraseña', 1, 'eledu@ejemplogmail5.com', 6),
    ('No me aparece Wind breaker en la busqueda', 1, 'auronplay@ejemplogmail3.com', 5),
	('¿Como quito un anime de favoritos?', 1, 'eledu@ejemplogmail5.com', 7),
	('No me deja subir una imagen a mi perfil', 0, 'auronplay@ejemplogmail3.com', 7),
    ('Porque no puedo entrar a mi cuenta????????', 0, 'carola@ejemplogmail4.com', 6),
    ('¿Cuando se podra ver One piece?', 0, 'anaisabelgarmendia@ejemplogmail6.com', 3),
    ('¿Como puedo hacer mas grande la letra de los subtitulos?', 0, 'eledu@ejemplogmail5.com', 7);

-- Inserciones a la tabla vistoEpisodioUsuario
INSERT INTO vistoEpisodioUsuario (usuarioId, animeId, numEpisodio, habilitado) VALUES 
	(1, 1, 1, 1),
	(1, 1, 2, 1),
	(1, 1, 3, 1),
	(1, 2, 1, 1),
	(1, 2, 2, 1),
	(1, 2, 3, 1),
	(1, 3, 1, 1),
	(1, 3, 2, 1),
	(1, 3, 3, 1),
	(1, 4, 1, 1),
	(1, 4, 2, 1),
	(1, 4, 3, 1),
	(1, 5, 1, 1),
	(1, 5, 2, 1),
	(1, 5, 3, 1),
	(1, 5, 4, 1),
	(1, 5, 5, 1),
	(1, 5, 6, 1),
	(1, 5, 7, 1),
	(1, 5, 8, 1),
	(1, 5, 9, 1),
	(1, 6, 1, 1),
	(1, 6, 2, 1),
	(1, 6, 3, 1),
	(1, 7, 1, 1),
	(1, 7, 2, 1),
	(1, 7, 3, 1),
	(4, 8, 1, 1),
	(4, 8, 2, 1),
	(4, 8, 3, 1),
	(4, 8, 4, 1),
	(4, 8, 5, 1),
	(4, 8, 6, 1),
	(4, 8, 7, 1),
	(4, 8, 8, 1),
	(4, 8, 9, 1),
	(4, 8, 10, 1),
	(4, 8, 11, 1),
	(4, 8, 12, 1),
	(4, 8, 13, 1);