from __future__ import annotations

import tkinter as tk
from dataclasses import dataclass
from datetime import date, timedelta
from tkinter import messagebox, ttk
from uuid import uuid4


@dataclass(frozen=True)
class Room:
    name: str
    description: str
    nightly_rate: float
    capacity: int

    def __str__(self) -> str:
        return f"{self.name}  |  ${self.nightly_rate:.0f}/night  |  {self.description}"


ROOMS = (
    Room("Garden King", "King bed · Garden view", 189, 2),
    Room("Harbor Suite", "Queen bed · Balcony", 265, 3),
    Room("Family Loft", "Two beds · City view", 320, 4),
)


class HotelBookingApp:
    def __init__(self, root: tk.Tk) -> None:
        self.root = root
        self.bookings: list[dict[str, object]] = []
        self.root.title("Juniper House | Stay booking")
        self.root.geometry("720x650")
        self.root.minsize(620, 570)
        self.root.configure(bg="#f8f7f1")

        self.room_var = tk.StringVar(value=str(ROOMS[0]))
        self.check_in_var = tk.StringVar()
        self.check_out_var = tk.StringVar()
        self.guest_var = tk.IntVar(value=2)
        self.estimate_var = tk.StringVar()
        self.status_var = tk.StringVar(value="Choose your stay details to see an estimate.")
        self._set_default_dates()
        self._build_ui()
        self._update_estimate()

    def _set_default_dates(self) -> None:
        self.check_in_var.set((date.today() + timedelta(days=7)).isoformat())
        self.check_out_var.set((date.today() + timedelta(days=9)).isoformat())

    def _build_ui(self) -> None:
        style = ttk.Style()
        style.theme_use("clam")
        style.configure("TCombobox", padding=8, font=("Segoe UI", 11))
        style.configure("TSpinbox", padding=8, font=("Segoe UI", 11))

        page = tk.Frame(self.root, bg="#f8f7f1", padx=42, pady=34)
        page.pack(fill="both", expand=True)

        tk.Label(
            page,
            text="JUNIPER HOUSE  /  PORTLAND, MAINE",
            bg="#f8f7f1",
            fg="#276953",
            font=("Segoe UI", 10, "bold"),
        ).pack(anchor="w")
        tk.Label(
            page,
            text="Make room for a getaway.",
            bg="#f8f7f1",
            fg="#203031",
            font=("Georgia", 28),
        ).pack(anchor="w", pady=(16, 5))
        tk.Label(
            page,
            text="A quiet corner by the coast, ready when you are.",
            bg="#f8f7f1",
            fg="#657471",
            font=("Segoe UI", 11),
        ).pack(anchor="w", pady=(0, 27))

        form = tk.Frame(page, bg="#f8f7f1")
        form.pack(fill="x")
        self._field_label(form, "ROOM", 0)
        self.room_picker = ttk.Combobox(
            form,
            textvariable=self.room_var,
            values=[str(room) for room in ROOMS],
            state="readonly",
        )
        self.room_picker.grid(row=0, column=1, sticky="ew", pady=8)

        self._field_label(form, "CHECK IN  (YYYY-MM-DD)", 1)
        check_in = ttk.Entry(form, textvariable=self.check_in_var, font=("Segoe UI", 11))
        check_in.grid(row=1, column=1, sticky="ew", pady=8, ipady=7)

        self._field_label(form, "CHECK OUT  (YYYY-MM-DD)", 2)
        check_out = ttk.Entry(form, textvariable=self.check_out_var, font=("Segoe UI", 11))
        check_out.grid(row=2, column=1, sticky="ew", pady=8, ipady=7)

        self._field_label(form, "GUESTS", 3)
        guests = ttk.Spinbox(form, from_=1, to=8, textvariable=self.guest_var, width=8)
        guests.grid(row=3, column=1, sticky="w", pady=8, ipady=5)
        form.columnconfigure(1, weight=1)

        for variable in (self.room_var, self.check_in_var, self.check_out_var, self.guest_var):
            variable.trace_add("write", self._update_estimate)

        divider = tk.Frame(page, bg="#dedfd6", height=1)
        divider.pack(fill="x", pady=(28, 17))

        tk.Label(
            page,
            textvariable=self.estimate_var,
            bg="#f8f7f1",
            fg="#203031",
            font=("Segoe UI", 16, "bold"),
        ).pack(anchor="w", pady=(0, 13))
        tk.Button(
            page,
            text="Reserve this stay",
            command=self._create_booking,
            bg="#276953",
            fg="white",
            activebackground="#1d5844",
            activeforeground="white",
            relief="flat",
            cursor="hand2",
            font=("Segoe UI", 12, "bold"),
            padx=18,
            pady=12,
        ).pack(fill="x")
        tk.Label(
            page,
            textvariable=self.status_var,
            bg="#f8f7f1",
            fg="#657471",
            font=("Segoe UI", 9),
            wraplength=610,
        ).pack(anchor="w", pady=(13, 4))

        tk.Label(
            page,
            text="RECENT BOOKINGS",
            bg="#f8f7f1",
            fg="#276953",
            font=("Segoe UI", 9, "bold"),
        ).pack(anchor="w", pady=(22, 8))
        self.booking_list = tk.Listbox(
            page,
            height=4,
            bg="white",
            fg="#203031",
            relief="flat",
            font=("Segoe UI", 10),
            activestyle="none",
            highlightthickness=1,
            highlightbackground="#dedfd6",
        )
        self.booking_list.pack(fill="both", expand=True)

    @staticmethod
    def _field_label(parent: tk.Widget, text: str, row: int) -> None:
        tk.Label(
            parent,
            text=text,
            bg="#f8f7f1",
            fg="#657471",
            font=("Segoe UI", 9, "bold"),
        ).grid(row=row, column=0, sticky="w", padx=(0, 18), pady=8)

    def _selected_room(self) -> Room:
        selection = self.room_var.get()
        return next(room for room in ROOMS if str(room) == selection)

    def _booking_details(self) -> tuple[Room, date, date, int, int, float]:
        try:
            check_in = date.fromisoformat(self.check_in_var.get().strip())
            check_out = date.fromisoformat(self.check_out_var.get().strip())
            guests = int(self.guest_var.get())
        except (ValueError, tk.TclError):
            raise ValueError("Enter valid dates as YYYY-MM-DD and a whole-number guest count.") from None

        if check_in <= date.today():
            raise ValueError("Check-in must be a future date.")
        if check_out <= check_in:
            raise ValueError("Check-out must be after check-in.")
        if not 1 <= guests <= 8:
            raise ValueError("Guest count must be between 1 and 8.")

        room = self._selected_room()
        if guests > room.capacity:
            raise ValueError(f"{room.name} accommodates up to {room.capacity} guests.")
        nights = (check_out - check_in).days
        return room, check_in, check_out, guests, nights, nights * room.nightly_rate

    def _update_estimate(self, *_args: object) -> None:
        try:
            _, _, _, _, nights, total = self._booking_details()
        except (ValueError, StopIteration) as error:
            self.estimate_var.set("")
            self.status_var.set(str(error))
            return
        self.estimate_var.set(f"{nights} nights  |  ${total:,.2f} total")
        self.status_var.set("Rates include your full stay. No payment is collected in this demo.")

    def _create_booking(self) -> None:
        try:
            room, check_in, check_out, guests, nights, total = self._booking_details()
        except (ValueError, StopIteration) as error:
            messagebox.showwarning("Check your booking", str(error), parent=self.root)
            return

        reference = uuid4().hex[:8].upper()
        booking = {
            "reference": reference,
            "room": room.name,
            "check_in": check_in,
            "check_out": check_out,
            "guests": guests,
            "total": total,
        }
        self.bookings.append(booking)
        self.booking_list.insert(
            0,
            f"{reference}  ·  {room.name}  ·  {check_in} to {check_out}  ·  ${total:,.2f}",
        )
        self.status_var.set(f"Booking confirmed · {reference}")
        messagebox.showinfo(
            "Booking confirmed",
            f"Your stay is reserved.\n\n{room.name} for {guests} guests\n"
            f"{nights} nights · ${total:,.2f}\nReference: {reference}",
            parent=self.root,
        )


def main() -> None:
    root = tk.Tk()
    HotelBookingApp(root)
    root.mainloop()


if __name__ == "__main__":
    main()