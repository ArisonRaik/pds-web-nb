USE `dockercon2035`;

CREATE TABLE `mensagem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idPaciente` bigint(20) NOT NULL,
  `idAlerta` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
