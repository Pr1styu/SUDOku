package hu.bme.compsec.sudoku.caffservice.service.processor;

public enum ParseResult {

    PARSED(0),
    CANNOT_OPEN(5);



    ParseResult(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

// 0 OK
//    1 -i kapcsoló nem található.
//            2 Nem megfelelő az argumentumok száma (3, 5 vagy 7 lehet a kapcsolókkal és a program elérési útjával együtt).
//            3 Az input fájl nem nyitható meg.
//            4 Az argumentumok formátuma hibás (Az -i kapcsoló rossz helyen van 3 argumentum esetében)
//5 Az input fájl nem nyitható meg (>3 argumentum esetén).
//            6 Az -i kapcsoló az argumentum lista legvégén található.
//8 A kimenet elérési útjában nem szerepel a fájl neve.
//9 A kimeti fájlnak hibás a kiterjesztése (pl.: jpeg helyett png).
//            10 Az -of kapcsoló az agrumentum lista legvégén található.
//11 Az -ot kapcsoló az agrumentum lista legvégén található.
//50 A CAFF fájl első blokkja nem a fejléc.
//            51 A magic rész nem a CAFF szót tartalmazza.
//            52 A második blokk nem a credits blokk.
//53 Nem animációs (CIFF) blokk az adott blokk.
//            54 A magic rész az animációs blokkon belül nem a CIFF értéket tartalmazza.
//            55 Hibás tartalom méret.
//56 A fájlvége és az olvasás vége nem ugyanott található
//57 Nem nyitható meg a jpeg kimeneti fájl (pl.: Hiányzó mappák miatt)
//58 Nem nyitható meg a txt kimeneti fájl (pl.: Hiányzó mappák miatt)

}
