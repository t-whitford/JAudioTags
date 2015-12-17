package ID3.v1;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Tom Whitford on 11/12/2015.
 */
@SuppressWarnings("unused")
public enum GenreEnum {

    BLUES(0),
    CLASSICROCK(1),
    COUNTRY(2),
    DANCE(3),
    DISCO(4),
    FUNK(5),
    GRUNGE(6),
    HIPHOP(7),
    JAZZ(8),
    METAL(9),
    NEWAGE(10),
    OLDIES(11),
    OTHER(12),
    POP(13),
    RNB(14),
    RAP(15),
    REGGAE(16),
    ROCK(17),
    TECHNO(18),
    INDUSTRIAL(19),
    ALTERNATIVE(20),
    SKA(21),
    DEATHMETAL(22),
    PRANKS(23),
    SOUNDTRACK(24),
    EUROTECHNO(25),
    AMBIENT(26),
    TRIPHOP(27),
    VOCAL(28),
    JAZZnFUNK(29),
    FUSION(30),
    TRANCE(31),
    CLASSICAL(32),
    INSTRUMENTAL(33),
    ACID(34),
    HOUSE(35),
    GAME(36),
    SOUNDCLIP(37),
    GOSPEL(38),
    NOISE(39),
    ALTERNROCK(40),
    BASS(41),
    SOUL(42),
    PUNK(43),
    SPACE(44),
    MEDITATIVE(45),
    INSTRUMENTALPOP(46),
    INSTRUMENTALROCK(47),
    ETHNIC(48),
    GOTHIC(49),
    DARKWAVE(50),
    TECHNOINDUSTRIAL(51),
    ELECTRONIC(52),
    POPFOLK(53),
    EURODANCE(54),
    DREAM(55),
    SOUTHERNROCK(56),
    COMEDY(57),
    CULT(58),
    GANGSTA(59),
    TOP40(60),
    CHRISTIANRAP(61),
    POPFUNK(62),
    JUNGLE(63),
    NATIVEAMERICAN(64),
    CABARET(65),
    NEWWAVE(66),
    PSYCHADELIC(67),
    RAVE(68),
    SHOWTUNES(69),
    TRAILER(70),
    LOFI(71),
    TRIBAL(72),
    ACIDPUNK(73),
    ACIDJAZZ(74),
    POLKA(75),
    RETRO(76),
    MUSICAL(77),
    ROCKROLL(78),
    HARDROCK(79),
    FOLK(80),
    FOLKROCK(81),
    NATIONALFOLK(82),
    SWING(83),
    FASTFUSION(84),
    BEBOB(85),
    LATIN(86),
    REVIVAL(87),
    CELTIC(88),
    BLUEGRASS(89),
    AVANTGARDE(90),
    GOTHICROCK(91),
    PROGRESSIVEROCK(92),
    PSYCHEDELICROCK(93),
    SYMPHONICROCK(94),
    SLOWROCK(95),
    BIGBAND(96),
    CHORUS(97),
    EASYLISTENING(98),
    ACOUSTIC(99),
    HUMOUR(100),
    SPEECH(101),
    CHANSON(102),
    OPERA(103),
    CHAMBERMUSIC(104),
    SONATA(105),
    SYMPHONY(106),
    BOOTYBASS(107),
    PRIMUS(108),
    PORNGROOVE(109),
    SATIRE(110),
    SLOWJAM(111),
    CLUB(112),
    TANGO(113),
    SAMBA(114),
    FOLKLORE(115),
    BALLAD(116),
    POWERBALLAD(117),
    RHYTHMICSOUL(118),
    FREESTYLE(119),
    DUET(120),
    PUNKROCK(121),
    DRUMSOLO(122),
    ACAPELLA(123),
    EUROHOUSE(124),
    DANCEHALL(125);

    private final int ID;

    private static final HashMap<Integer, GenreEnum> lookup = new HashMap<Integer, GenreEnum>();

    static {
        for(GenreEnum g: EnumSet.allOf(GenreEnum.class))
            lookup.put(g.getCode(), g);
    }

    private int getCode()
    {
        return ID;
    }

    GenreEnum(int id) {
        ID = id;
    }

    public static GenreEnum get(int code)
    {
        return lookup.get(code);
    }

}
