import React from "react";
import './StatItem.css';

function StatItem(props) {

    const { title, data } = props

    return (
        <div className={`Stat-item`}>
            <span className={`Stat-item-title`}>{`${title}:`}</span>
            {data.map((it) => <div className={`Stat-data-item`}><span>{`${it.value}: ${it.count}`}</span></div>)}
        </div>
    )

}

export default StatItem