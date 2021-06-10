package fr.univlyon1.mif37.dex.mapping.topDown;

import java.util.List;
/**
 * @juba BDD
 */
/**
 * An adorned tgd (i.e., a Horn clause where every atom is itself adorned).
 *
 */

public class AdornedTgd {
    private final AdornedAtom head;
    private final List<AdornedAtom> body;

    /**
     * Constructs an adorned tgd given an adorned atom for the head and a
     * list of adorned atoms for the body.
     *
     * @param head
     *            head atom of clause
     * @param body
     *            atoms for body of clause
     */
    public AdornedTgd(AdornedAtom head, List<AdornedAtom> body) {
        this.head = head;
        this.body = body;
    }

    /**
     * Get.
     * @return AdornedTgd's head
     */
    public AdornedAtom getHead() {
        return head;
    }

    /**
     * Get.
     * @return AdornedTgd's body
     */
    public List<AdornedAtom> getBody() {
        return body;
    }

    @Override
    public String toString() {
        String str = "";
        for (AdornedAtom adornedAtom: body) {
            str += adornedAtom.toString() + " , ";
        }
        if (str.length() > 2) {
            str = str.substring(0, str.length() - 3);
        }
        str += " -> " + head.toString();
        return str;
    }



}
