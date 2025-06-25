package co.morillas.repository

import co.morillas.core.domain.Cart
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<Cart, Long>