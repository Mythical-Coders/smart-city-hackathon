import React, { useState } from "react";
import GraphPercentageFullness from "./content/GraphPercentageFullness";
import Button from "../../components/CustomButtons/Button";
import { RegionDropdown } from "react-country-region-selector";
import DateTimePicker from "react-datetime-picker";
import NavPills from "../../components/NavPills/NavPills";
import { Schedule, Dashboard } from "@material-ui/icons";
import MostImpoundPlaces from "./content/MostImpoundPlaces";
function Statictics() {
  const [dateStartMostImpound, setDateStartMostImpound] = useState(new Date());
  const [dateEndMostImpound, setDateEndMostImpound] = useState(new Date());
  const [dateStartFullness, setDateStartFullness] = useState(new Date());
  const [dateEndFullness, setDateEndFullness] = useState(new Date());
  // const [regionMostImpound, setRegionMostImpound] = useState(null);
  // const [regionFullness, setRegionFullness] = useState(null);


  const handleChange = (e, name) => {
    const date = {};
    date[name] = e;
    switch (name) {
      case "dateStartMostImpound":
        setDateStartMostImpound(new Date(date.dateStartMostImpound));
        break;
      case "dateEndMostImpound":
        setDateEndMostImpound(new Date(date.dateEndMostImpound));
        break;
        case "dateStartFullness":
          setDateStartFullness(new Date(date.dateStartFullness));
        break;
      case "dateEndFullness":
        setDateEndFullness(new Date(date.dateEndFullness));
        break;
      default:
        break;
    }
  };
  return (
    <NavPills
      active={1}
      color="chengapp"
      alignCenter
      tabs={[
        {
          tabButton: "أكثر الاماكن مخالفة ",
          tabIcon: Dashboard,
          tabContent: (
            <>
              <div
                style={{
                  backgroundColor: "#D3D3D3",
                  borderRadius: "10px",
                  textAlign: "right",
                  padding: "15px",
                }}
              >
                <h1>أكثر الاماكن مخالفة </h1>
                <h3> :المنطقة</h3>
                <RegionDropdown
                  defaultOptionLabel="اختر المنطقة"
                  id="region"
                  name="region"
                  country="Tunisia"
                  // value={regionMostImpound}
                  // onChange={(e) => handleChange(e, "regionMostImpound")}
                />
                <h3> :وقت البدء</h3>
                <DateTimePicker
                onChange={(e) => handleChange(e, "dateStartMostImpound")}
                value={dateStartMostImpound}
                />
                <h3> :نهاية الوقت</h3>
                <DateTimePicker
                onChange={(e) => handleChange(e, "dateEndMostImpound")}
                value={dateEndMostImpound}
                />
                <br /> <br />
                <Button variant="contained" color="info">
                  اختر
                </Button>
                <br />
                <br />
                <br />
                <MostImpoundPlaces />
              </div>
            </>
          ),
        },
        {
          tabButton: "نسبة إمتلاء مركز الحجز ",

          tabIcon: Schedule,
          tabContent: (
            <>
              <div
                style={{
                  backgroundColor: "#D3D3D3",
                  borderRadius: "10px",
                  textAlign: "right",
                  padding: "15px",
                }}
              >
                <h1>نسبة إمتلاء مركز الحاجز</h1>
                <h3> :المنطقة</h3>
                <RegionDropdown
                  defaultOptionLabel="اختر المنطقة"
                  id="region"
                  name="region"
                  country="Tunisia"
                  // value={regionFullness}
                  // onChange={(e) => handleChange(e, "regionFullness")}
                />
                <h3> :وقت البدء</h3>
                <DateTimePicker
                onChange={(e) => handleChange(e, "dateStartFullness")}
                value={dateStartFullness}
                />
                <h3> :نهاية الوقت</h3>
                <DateTimePicker
                onChange={(e) => handleChange(e, "dateEndFullness")}
                value={dateEndFullness}
                />
                <br /> <br />
                <Button variant="contained" color="info">
                  اختر
                </Button>
                <br />
                <br />
                <br />
                <GraphPercentageFullness />
              </div>
            </>
          ),
        },
      ]}
    />
  );
}

export default Statictics;
