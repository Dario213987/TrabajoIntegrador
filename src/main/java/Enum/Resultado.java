package Enum;

import lombok.Getter;

public enum Resultado {
    ganaEquipo1(4),
    empate(5),
    ganaEquipo2(6);
    @Getter
    final int ubicacion;
    Resultado(int ubicacion){
        this.ubicacion =ubicacion;
    }
}