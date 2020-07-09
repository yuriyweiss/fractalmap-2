CREATE  TABLE `fractalmap`.`square` (
  `layer_index` INT NOT NULL ,
  `left_re` DOUBLE NOT NULL ,
  `top_im` DOUBLE NOT NULL ,
  `iterations` INT NOT NULL ,
  PRIMARY KEY (`layer_index`, `left_re`, `top_im`) )
ENGINE = InnoDB;

CREATE  TABLE `fractalmap`.`text_object` (
  `re` DOUBLE NOT NULL ,
  `im` DOUBLE NOT NULL ,
  `min_layer_index` INT NOT NULL ,
  `text` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`re`, `im`) )
ENGINE = InnoDB;