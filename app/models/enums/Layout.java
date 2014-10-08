package models.enums;

public enum Layout {
    NORMAL          ("normal"),
    SPLIT           ("split"),
    FLIP            ("flip"),
    DOUBLE_FACED    ("double-faced"),
    TOKEN           ("token"),
    PLANE           ("plane"),
    SCHEME          ("scheme"),
    PHENOMENON      ("phenomenon"),
    LEVELER         ("leveler"),
    VANGUARD        ("vanguard");

    private final String value;

    private Layout(String s){
        value = s;
    }

    public boolean equals(String s){
        return (s == null)? false : value.equals(s);
    }

    public String toString(){
        return value;
    }
}
