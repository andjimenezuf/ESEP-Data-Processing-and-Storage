public class Main {
    public static void main(String[] args) {
        InMemoryDB db = new InMemoryDBImpl();

        // should return null, because A doesnt exist
        System.out.println(db.get("A"));

        // error because transaction not in progress
        try {
            db.put("A", 5);
        } catch (Exception e) {
            System.out.println(e.getMessage()); // cannot perform put.
        }
        db.begin_transaction();
        db.put("A", 5);

        // null, changes not committed yet
        System.out.println(db.get("A"));

        // update A to 6
        db.put("A", 6);

        // commit change
        db.commit();

        //return 6
        System.out.println(db.get("A"));

        // error, no more open transaction
        try {
            db.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.rollback();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // null, B does not exist
        System.out.println(db.get("B"));

        db.begin_transaction();
        db.put("B", 10);
        db.rollback();

        // null, changes to B were rolled back
        System.out.println(db.get("B"));
    }
}
